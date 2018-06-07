package neo.script.gorm.graphql

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration
import graphql.schema.GraphQLSchema
import neo.script.gorm.graphql.binding.GormGraphQLDataBinder
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.graphql.fetcher.GormGraphQLDataFetcherManager
import neo.script.gorm.graphql.helper.MappingHelper
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.gorm.graphql.GraphQLEntityHelper
import org.grails.gorm.graphql.Schema
import org.grails.gorm.graphql.binding.manager.DefaultGraphQLDataBinderManager
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.context.ApplicationContext
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

    @Autowired
    ApplicationContext applicationContext

    @Bean
    GraphQLSchema graphQLSchema(HibernateDatastore datastore) {
        MappingHelper.mappingPreprocess(applicationContext,datastore)
        def schema = new Schema(datastore.mappingContext)

        schema.dataFetcherManager = new GormGraphQLDataFetcherManager();
        schema.dataBinderManager = new DefaultGraphQLDataBinderManager(new GormGraphQLDataBinder());
        schema.initialize()
        //listArguments只需criteria，这个需在initialize之后设置
        schema.listArguments = ['criteria': String]
        schema.generate()
    }
}
