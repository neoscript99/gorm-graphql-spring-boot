package neo.script.gorm.graphql

import com.oembedler.moon.graphql.boot.GraphQLWebAutoConfiguration
import graphql.schema.GraphQLSchema
import neo.script.gorm.graphql.binding.GormGraphQLDataBinder
import neo.script.gorm.graphql.fetcher.GormGraphQLDataFetcherManager
import neo.script.gorm.graphql.helper.MappingHelper
import org.grails.gorm.graphql.Schema
import org.grails.gorm.graphql.binding.manager.DefaultGraphQLDataBinderManager
import org.grails.gorm.graphql.entity.GraphQLEntityNamingConvention
import org.grails.gorm.graphql.entity.property.manager.DefaultGraphQLDomainPropertyManager
import org.grails.gorm.graphql.entity.property.manager.GraphQLDomainPropertyManager
import org.grails.gorm.graphql.fetcher.manager.GraphQLDataFetcherManager
import org.grails.gorm.graphql.response.errors.DefaultGraphQLErrorsResponseHandler
import org.grails.gorm.graphql.response.errors.GraphQLErrorsResponseHandler
import org.grails.gorm.graphql.response.pagination.DefaultGraphQLPaginationResponseHandler
import org.grails.gorm.graphql.response.pagination.GraphQLPaginationResponseHandler
import org.grails.gorm.graphql.types.DefaultGraphQLTypeManager
import org.grails.gorm.graphql.types.GraphQLTypeManager
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.StaticMessageSource

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
    GraphQLEntityNamingConvention graphQLEntityNamingConvention() {
        new GraphQLEntityNamingConvention()
    }

    @Bean
    GraphQLErrorsResponseHandler graphQLErrorsResponseHandler() {
        new DefaultGraphQLErrorsResponseHandler(new StaticMessageSource())
    }

    @Bean
    GraphQLDomainPropertyManager graphQLDomainPropertyManager() {
        new DefaultGraphQLDomainPropertyManager()
    }

    @Bean
    GraphQLPaginationResponseHandler graphQLPaginationResponseHandler() {
        new DefaultGraphQLPaginationResponseHandler()
    }

    @Bean
    GraphQLTypeManager graphQLTypeManager(GraphQLEntityNamingConvention namingConvention,
                                          GraphQLErrorsResponseHandler errorsResponseHandler,
                                          GraphQLDomainPropertyManager domainPropertyManager,
                                          GraphQLPaginationResponseHandler paginationResponseHandler) {
        new DefaultGraphQLTypeManager(namingConvention, errorsResponseHandler, domainPropertyManager, paginationResponseHandler)
    }

    @Bean
    Schema gormSchema(HibernateDatastore datastore, GraphQLEntityNamingConvention namingConvention,
                      GraphQLErrorsResponseHandler errorsResponseHandler,
                      GraphQLDomainPropertyManager domainPropertyManager,
                      GraphQLPaginationResponseHandler paginationResponseHandler,
                      GraphQLTypeManager typeManager
    ) {
        MappingHelper.mappingPreprocess(applicationContext, datastore)
        GraphQLDataFetcherManager dataFetcherManager = new GormGraphQLDataFetcherManager();
        MappingHelper.dataFetcherPreprocess(applicationContext, dataFetcherManager);

        def schema = new Schema(datastore.mappingContext);
        schema.dataFetcherManager = dataFetcherManager;
        schema.namingConvention = namingConvention
        schema.errorsResponseHandler = errorsResponseHandler
        schema.domainPropertyManager = domainPropertyManager
        schema.paginationResponseHandler = paginationResponseHandler
        schema.typeManager = typeManager
        schema.dataBinderManager = new DefaultGraphQLDataBinderManager(new GormGraphQLDataBinder());
        //必须在schema.initialize()之前
        schema.dateFormatLenient = true
        schema.dateFormats = ["yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"]
        schema.initialize()
        //listArguments只需criteria，这个需在initialize之后设置
        schema.listArguments = ['criteria': String]

        MappingHelper.typePreprocess(applicationContext, schema.typeManager)
        return schema
    }

    @Bean
    GraphQLSchema graphQLSchema(Schema schema) {
        schema.generate()
    }
}
