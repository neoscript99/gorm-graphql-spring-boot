package neo.script.gorm.graphql.security

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.NoOpInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.schema.DataFetcher
import groovy.util.logging.Slf4j
import neo.script.gorm.graphql.execution.GormAbortExecutionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.Duration
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
            throw new GormAbortExecutionException('无token', 'TokenError');

        //Variables包含token信息，put后传递给后续instrument
        (parameters.getInstrumentationState() as Map).putAll(parameters.getVariables())
        def operation = parameters.operation ?: parameters.query.substring(0, parameters.query.indexOf("{")).trim()
        def beginTime = LocalDateTime.now();
        log.info("Query '{}' 开始执行时间：{}.", operation, beginTime)
        return new InstrumentationContext<ExecutionResult>() {
            @Override
            void onEnd(ExecutionResult result, Throwable t) {
                def endTime = LocalDateTime.now();
                def duration = Duration.between(beginTime, endTime)
                log.info("Query '{}' 结束执行时间：{}.", operation, endTime)
                log.info("Query '{}' 执行用时 {}秒 {}毫秒.",
                        operation,
                        duration.seconds, duration.toMillis() % 1000)
            }
        };
    }


    @Override
    DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {

        String token = (parameters.getInstrumentationState() as Map).get('token');
        if (domainAuthorization && !domainAuthorization.isAuthorized(dataFetcher, parameters, token))
            throw new GormAbortExecutionException('token无效', 'TokenError');

        return super.instrumentDataFetcher(dataFetcher, parameters)
    }

    static class MapState extends HashMap<String, Object> implements InstrumentationState {}

}
