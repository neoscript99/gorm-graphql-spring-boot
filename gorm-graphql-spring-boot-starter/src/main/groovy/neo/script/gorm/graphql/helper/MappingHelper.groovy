package neo.script.gorm.graphql.helper

import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.gorm.graphql.GraphQLEntityHelper
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.entity.dsl.LazyGraphQLMapping
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
}

/**
 *  Because we are accessing the persistent entity,
 *  the GORM mapping context must be created before this code is evaluated
 */
class LazyMapping extends LazyGraphQLMapping {
    Class<GormEntity> mappingClass

    protected LazyMapping(Class<GraphQLMapping> mappingClass) {
        super(null)
        this.mappingClass = mappingClass
    }

    @Override
    GraphQLMapping initialize() {
        mappingClass.newInstance()
    }
}