package neo.script.gorm.graphql.execution

import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation
import org.springframework.beans.BeanUtils

/**
 * 将ExceptionWhileDataFetching、Throwable等去除调用堆栈并返回客户端
 */
class ClientGraphQLError implements GraphQLError {
    ClientGraphQLError(GraphQLError error) {
        BeanUtils.copyProperties(error, this)
    }
    String errorCode
    String message
    List<SourceLocation> locations
    ErrorType errorType
    List<Object> path
}
