package neo.script.gorm.graphql.schema

import org.grails.gorm.graphql.types.GraphQLTypeManager

interface GraphQLTypeRegister {
    void doRegister(GraphQLTypeManager typeManager)
}
