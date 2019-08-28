package neo.script.gorm.general.graphql

import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import neo.script.gorm.graphql.security.ExecutionAuthorization
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ExecutionAuthorizationImpl implements ExecutionAuthorization {
    //graphiql.enabled一般会在开发时开启
    @Value('${graphiql.enabled}')
    Boolean graphiqlEnabled

    @Override
    boolean isAuthorized(InstrumentationExecutionParameters parameters) {
        (graphiqlEnabled
                || parameters.variables.token
                || DomainAuthorizationImpl.SKIP_TOKEN_OPERATION.contains(parameters.operation))
    }
}
