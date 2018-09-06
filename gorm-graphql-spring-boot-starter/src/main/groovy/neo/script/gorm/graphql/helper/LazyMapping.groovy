package neo.script.gorm.graphql.helper

import org.grails.datastore.gorm.GormEntity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.grails.gorm.graphql.entity.dsl.LazyGraphQLMapping

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