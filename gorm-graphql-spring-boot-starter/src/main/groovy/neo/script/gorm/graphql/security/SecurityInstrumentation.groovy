package neo.script.gorm.graphql.security

import graphql.ExecutionResult
import graphql.execution.AbortExecutionException
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.NoOpInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.schema.DataFetcher
import groovy.util.logging.Slf4j
import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.fetcher.interceptor.InterceptingDataFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Duration

/**
 * 系统安全校验介入器
 *
 * 将自动被GraphQLWebAutoConfiguration引用
 * @see com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration
 *
 * DataFetcherInterceptor会过滤变量，environment.getArguments()不包含token
 */
@Component
@Slf4j
class SecurityInstrumentation extends NoOpInstrumentation {
    @Autowired(required = false)
    DomainAuthorization domainAuthorization

    @Override
    InstrumentationState createState() {
        new MapState();
    }

    @Override
    InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        if (parameters.operation && parameters.operation != 'IntrospectionQuery' && !parameters.variables.token)
            throw new AbortExecutionException('无token');
        //Variables包含token信息
        (parameters.getInstrumentationState() as Map).putAll(parameters.getVariables())
        long startNanos = System.nanoTime();
        return new InstrumentationContext<ExecutionResult>() {
            @Override
            void onEnd(ExecutionResult result, Throwable t) {
                log.debug("Query '{}' execution duration is {} seconds.",
                        parameters.operation ?: parameters.query.substring(0, parameters.query.indexOf("{")).trim(),
                        Duration.ofNanos(System.nanoTime() - startNanos).seconds)
            }
        };
    }


    @Override
    DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        //InterceptingDataFetcher为gorm根据domain类自动生成的DataFetcher
        //如果要校验其它操作，需优化这里的处理
        String token = (parameters.getInstrumentationState() as Map).get('token');
        if (domainAuthorization && InterceptingDataFetcher.isAssignableFrom(dataFetcher.class)) {
            Class clazz;
            GraphQLDataFetcherType fetcherType;
            InterceptingDataFetcher.declaredFields.each {
                it.setAccessible(true)
                switch (it.name) {
                    case 'clazz':
                        clazz = it.get(dataFetcher);
                        break;
                    case 'fetcherType':
                        fetcherType = it.get(dataFetcher);
                        break;
                    default:
                        break;
                }
            };
            if (!domainAuthorization.authorization(token, clazz, MUTATION_TYPE_LIST.contains(fetcherType)))
                throw new AbortExecutionException('非法token');
        }
        return super.instrumentDataFetcher(dataFetcher, parameters)
    }

    static MUTATION_TYPE_LIST = [GraphQLDataFetcherType.CREATE, GraphQLDataFetcherType.DELETE, GraphQLDataFetcherType.UPDATE]
    static class MapState extends HashMap<String, Object> implements InstrumentationState {}
}
