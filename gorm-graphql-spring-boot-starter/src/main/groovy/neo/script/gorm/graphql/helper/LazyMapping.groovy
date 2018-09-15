package neo.script.gorm.graphql.helper

import org.grails.datastore.gorm.GormEntity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.entity.dsl.LazyGraphQLMapping

/**
 * 原先设计，需要GraphQLMapping.lazy的，可以通过 static graphql = new LazyMapping(UserGraphqlMapping.class),这样mapping可以写到单独的类中
 * 之后加了GraphQLMappingFlag注解，通过spring管理，能得到domain.gormPersistentEntity，所以暂时不再需要这个类
 *
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