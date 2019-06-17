package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.portal.domain.pt.pds.PortletDataSource

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'portletName')
@EqualsAndHashCode(includes = 'id')
class Portlet {
    String id
    String portletName
    String type
    PortletDataSource ds

    Date dateCreated
    Date lastUpdated

    static mapping = {
        ds fetch: 'join', lazy: false
    }

    static constraints = {
        ds nullable: true
    }

    static graphql = true
}
