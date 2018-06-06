package neo.script.gorm.graphql.demo.graphql

import graphql.ExecutionResult
import graphql.execution.AbortExecutionException
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.NoOpInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.execution.instrumentation.parameters.InstrumentationValidationParameters
import graphql.schema.DataFetcher
import graphql.validation.ValidationError
import groovy.util.logging.Slf4j
import org.grails.datastore.gorm.GormEntity
import org.grails.gorm.graphql.GraphQLServiceManager
import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.fetcher.interceptor.InterceptingDataFetcher
import org.grails.gorm.graphql.fetcher.interceptor.InterceptorInvoker
import org.springframework.stereotype.Component

import java.time.Duration

/**
 * 系统安全校验介入器
 *
 * DataFetcherInterceptor会过滤变量，environment.getArguments()不包含token
 */
@Component
@Slf4j
class SecurityInstrumentation extends NoOpInstrumentation {

    @Override
    InstrumentationState createState() {
        new MapState();
    }

    @Override
    InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        (parameters.getInstrumentationState() as Map).putAll(parameters.getVariables())
        long startNanos = System.nanoTime();
        return new InstrumentationContext<ExecutionResult>() {
            @Override
            void onEnd(ExecutionResult result, Throwable t) {
                log.debug("Query '{}' execution duration is {} seconds.",
                        parameters.getOperation(),
                        Duration.ofNanos(System.nanoTime() - startNanos).seconds)
            }
        };
    }

    /**
     * 根据token，domain和是否写操作进行签权
     * @param stateMap
     * @param domain
     * @param isMutation
     * @return
     */
    boolean authorization(String token, Class<GormEntity> domain, boolean isMutation) {
        if (token == 'bb')
            return true
        return false
    }

    @Override
    DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        if (InterceptingDataFetcher.isAssignableFrom(dataFetcher.class)) {
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
            if (!authorization((parameters.getInstrumentationState() as Map).get('token'),
                    clazz,
                    MUTATION_TYPE_LIST.contains(fetcherType))
            )
                throw new AbortExecutionException('非法token');
        }
        return super.instrumentDataFetcher(dataFetcher, parameters)
    }

    static MUTATION_TYPE_LIST = [GraphQLDataFetcherType.CREATE, GraphQLDataFetcherType.DELETE, GraphQLDataFetcherType.UPDATE]
    static class MapState extends HashMap<String, Object> implements InstrumentationState {}
}
