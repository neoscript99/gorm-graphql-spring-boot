package neo.script.gorm.graphql.security

import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters

interface ExecutionAuthorization {
    boolean isAuthorized(InstrumentationExecutionParameters parameters)
}
