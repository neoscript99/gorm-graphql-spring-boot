package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
/**
 * 定制的Portlet，通过type来做对应实现
 */
class PortletCustomize extends Portlet {
    static graphql = GraphQLMapping.build {
        description('自定义组件，前端需根据组件类型适配开发')
    }
}
