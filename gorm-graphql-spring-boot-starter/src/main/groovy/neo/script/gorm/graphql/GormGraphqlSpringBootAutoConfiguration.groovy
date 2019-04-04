package neo.script.gorm.graphql

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration
import graphql.schema.GraphQLSchema
import neo.script.gorm.graphql.binding.GormGraphQLDataBinder
import neo.script.gorm.graphql.fetcher.GormGraphQLDataFetcherManager
import neo.script.gorm.graphql.helper.MappingHelper
import org.grails.gorm.graphql.Schema
import org.grails.gorm.graphql.binding.manager.DefaultGraphQLDataBinderManager
import org.grails.gorm.graphql.fetcher.manager.GraphQLDataFetcherManager
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
        MappingHelper.mappingPreprocess(applicationContext, datastore)
        GraphQLDataFetcherManager dataFetcherManager = new GormGraphQLDataFetcherManager();
        MappingHelper.dataFetcherPreprocess(applicationContext, dataFetcherManager);

        def schema = new Schema(datastore.mappingContext);
        schema.dataFetcherManager = dataFetcherManager;
        schema.dataBinderManager = new DefaultGraphQLDataBinderManager(new GormGraphQLDataBinder());
        //必须在schema.initialize()之前
        schema.dateFormatLenient = true
        schema.dateFormats = ["yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"]
        schema.initialize()
        //listArguments只需criteria，这个需在initialize之后设置
        schema.listArguments = ['criteria': String]

        MappingHelper.typePreprocess(applicationContext, schema.typeManager)
        schema.generate()
    }
}
