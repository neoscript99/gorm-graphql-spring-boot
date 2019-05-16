package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'portletName')
@EqualsAndHashCode(includes = 'id')
class Portlet {
    String id
    String portletName
    Portal portal
    String type

    Date dateCreated
    Date lastUpdated

    static mapping = {
        portal fetch: 'join', lazy: false
    }
    static graphql = true
}
