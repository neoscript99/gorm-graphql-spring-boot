package neo.script.gorm.portal.domain.pt.plet

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
    String type

    Date dateCreated
    Date lastUpdated

    static graphql = true
}
