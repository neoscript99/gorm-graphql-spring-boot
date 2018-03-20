package neo.script.gorm.graphql

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.grails.gorm.graphql.Schema
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
/**
 * 创建spring boot自动配置
 */
@Configuration
@ComponentScan
@AutoConfigureBefore(GraphQLWebAutoConfiguration)
class GormGraphqlSpringBootAutoConfiguration {

    @Bean
    GraphQLSchema graphQLSchema(HibernateDatastore datastore) {
        new Schema(datastore.mappingContext).generate()
    }

    @Bean
    GraphQL graphQL(GraphQLSchema graphQLSchema) {
        new GraphQL(graphQLSchema)
    }
}
