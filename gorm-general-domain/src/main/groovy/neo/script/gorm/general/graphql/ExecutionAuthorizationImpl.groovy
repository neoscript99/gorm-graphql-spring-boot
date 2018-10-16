package neo.script.gorm.general.graphql

import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import neo.script.gorm.graphql.security.ExecutionAuthorization
import org.springframework.stereotype.Component

@Component
class ExecutionAuthorizationImpl implements ExecutionAuthorization {
    @Override
    boolean isAuthorized(InstrumentationExecutionParameters parameters) {
        (parameters.operation == null
                || parameters.operation == 'IntrospectionQuery'
                || parameters.operation == 'login'
                || parameters.operation == 'logout'
                || parameters.variables.token)
    }
}
