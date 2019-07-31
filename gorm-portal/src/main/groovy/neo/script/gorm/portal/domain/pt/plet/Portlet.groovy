package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.portal.domain.pt.pds.PortletDataSource
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'portletName')
@EqualsAndHashCode(includes = 'id')
class Portlet {
    String id
    String portletName
    String type
    PortletDataSource ds
    //json
    String style

    Date dateCreated
    Date lastUpdated

    static mapping = {
        ds fetch: 'join', lazy: false
    }

    static constraints = {
        ds nullable: true
        style nullable: true, maxSize: 256
    }

    static graphql = GraphQLMapping.build {
        description('Portlet组件基类')
        property('portletName') { description('组件名') }
        property('type') { description('组件类型') }
        property('ds') { description('数据源') }
    }
}
