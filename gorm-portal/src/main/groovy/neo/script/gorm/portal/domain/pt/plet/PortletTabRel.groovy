package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

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
    static graphql = true
}
