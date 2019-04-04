package neo.script.gorm.graphql.helper

import neo.script.gorm.graphql.entity.GraphQLDataFetcherFlag
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.graphql.schema.GraphQLTypeRegister
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.gorm.graphql.GraphQLEntityHelper
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.fetcher.BindingGormDataFetcher
import org.grails.gorm.graphql.fetcher.DeletingGormDataFetcher
import org.grails.gorm.graphql.fetcher.ReadingGormDataFetcher
import org.grails.gorm.graphql.fetcher.manager.GraphQLDataFetcherManager
import org.grails.gorm.graphql.types.GraphQLTypeManager
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.context.ApplicationContext

class MappingHelper {
    static LazyMapping lazy(Class<GraphQLMapping> mappingClass) {
        new LazyMapping(mappingClass)
    }

    /**
     * 1. paginate默认设为true
     * 2. 支持GraphQLMappingFlag注解
     * @param applicationContext
     * @param datastore
     */
    static void mappingPreprocess(ApplicationContext applicationContext, HibernateDatastore datastore) {

        applicationContext.getBeansWithAnnotation(GraphQLMappingFlag).each { name, bean ->
            //key为domain类，value为对应的GraphQLMapping bean
            bean.class.getAnnotation(GraphQLMappingFlag).value().graphql = bean
        };

        datastore.mappingContext.persistentEntities.each { PersistentEntity entity ->
            GraphQLMapping mapping = GraphQLEntityHelper.getMapping(entity)
            //默认list返回分页结果
            if (mapping)
                mapping.operations.list.paginate = true
        }
    }

    static void dataFetcherPreprocess(ApplicationContext applicationContext, GraphQLDataFetcherManager dataFetcherManager) {
        applicationContext.getBeansWithAnnotation(GraphQLDataFetcherFlag).each { name, bean ->
            //注册自定义DataFetcher，注意需实现supports方法
            //registerXxxDataFetcher中会判断fetcher.supports(GraphQLDataFetcherType)
            GraphQLDataFetcherFlag flag = bean.class.getAnnotation(GraphQLDataFetcherFlag);
            if (BindingGormDataFetcher.isAssignableFrom(bean.class))
                dataFetcherManager.registerBindingDataFetcher(flag.entityClass(), bean);
            if (ReadingGormDataFetcher.isAssignableFrom(bean.class))
                dataFetcherManager.registerReadingDataFetcher(flag.entityClass(), bean);
            if (DeletingGormDataFetcher.isAssignableFrom(bean.class))
                dataFetcherManager.registerDeletingDataFetcher(flag.entityClass(), bean);

        };
    }

    static void typePreprocess(ApplicationContext applicationContext, GraphQLTypeManager typeManager) {
        applicationContext.getBeansOfType(GraphQLTypeRegister).each { name, bean ->
            bean.doRegister(typeManager);
        }
    }
}
