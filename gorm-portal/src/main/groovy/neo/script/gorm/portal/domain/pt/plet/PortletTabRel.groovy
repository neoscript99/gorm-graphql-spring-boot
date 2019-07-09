package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
class PortletTabRel {
    String id

    PortletTab tab
    Portlet portlet
    Integer portletOrder = 0

    Date dateCreated
    Date lastUpdated

    static mapping = {
        tab fetch: 'join', lazy: false
        portlet fetch: 'join', lazy: false
        tab unique: 'portlet'
    }
    static graphql = GraphQLMapping.build {
        description('TAB页子组件关联信息')
        property('tab') { description('TAB页组件') }
        property('portlet') { description('子Portlet组件') }
        property('portletOrder') { description('子Portlet组件序号') }
    }

}
