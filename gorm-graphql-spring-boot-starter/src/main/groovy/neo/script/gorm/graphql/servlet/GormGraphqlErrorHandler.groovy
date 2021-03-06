package neo.script.gorm.graphql.servlet

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.servlet.DefaultGraphQLErrorHandler
import graphql.servlet.GenericGraphQLError
import neo.script.gorm.graphql.execution.ClientGraphQLError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GormGraphqlErrorHandler extends DefaultGraphQLErrorHandler {
    public static final Logger logger = LoggerFactory.getLogger(DefaultGraphQLErrorHandler.class);

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        errors.collect { error ->
            if (error instanceof Throwable) {
                logger.error("Error executing query!", (Throwable) error);
                return new ClientGraphQLError(error);
            } else if (error instanceof ExceptionWhileDataFetching) {
                logger.error("Error executing query ({}): {}", error.getClass().getSimpleName(), error.getMessage());
                return new ClientGraphQLError(error);
            }
            return error
        }
    }
}
