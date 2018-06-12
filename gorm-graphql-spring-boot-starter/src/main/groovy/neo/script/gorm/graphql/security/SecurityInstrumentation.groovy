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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime

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
    @Autowired(required = false)
    ExecutionAuthorization executionAuthorization

    @Override
    InstrumentationState createState() {
        new MapState();
    }

    @Override
    InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        //TODO:错误处理
        if (executionAuthorization && !executionAuthorization.isAuthorized(parameters))
            throw new AbortExecutionException('无token');

        //Variables包含token信息
        (parameters.getInstrumentationState() as Map).putAll(parameters.getVariables())
        def beginTime = LocalDateTime.now();
        return new InstrumentationContext<ExecutionResult>() {
            @Override
            void onEnd(ExecutionResult result, Throwable t) {
                def duration = Duration.between(beginTime, LocalDateTime.now())
                log.debug("Query '{}' execution duration is {} seconds plus {} milliseconds.",
                        parameters.operation ?: parameters.query.substring(0, parameters.query.indexOf("{")).trim(),
                        duration.seconds, duration.toMillis() % 1000)
            }
        };
    }


    @Override
    DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {

        String token = (parameters.getInstrumentationState() as Map).get('token');
        if (domainAuthorization && !domainAuthorization.isAuthorized(dataFetcher, parameters, token))
            throw new AbortExecutionException('非法token');

        return super.instrumentDataFetcher(dataFetcher, parameters)
    }

    static class MapState extends HashMap<String, Object> implements InstrumentationState {}

}
