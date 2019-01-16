package neo.script.gorm.general.graphql

import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import neo.script.gorm.graphql.security.ExecutionAuthorization
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile('prod')
class ExecutionAuthorizationImpl implements ExecutionAuthorization {
    @Override
    boolean isAuthorized(InstrumentationExecutionParameters parameters) {
        (parameters.operation == 'login'
                || parameters.operation == 'logout'
                || parameters.variables.token)
    }
}
