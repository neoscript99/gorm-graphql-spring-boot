package neo.script.gorm.graphql.execution

import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.execution.AbortExecutionException

class GormAbortExecutionException extends AbortExecutionException {
    GormAbortExecutionException(String message, String errorCode) {
        super(message)
        this.errorCode = errorCode
    }
    String errorCode;

    @Override
    @JsonIgnore
    StackTraceElement[] getStackTrace() {
        return super.getStackTrace()
    }
}
