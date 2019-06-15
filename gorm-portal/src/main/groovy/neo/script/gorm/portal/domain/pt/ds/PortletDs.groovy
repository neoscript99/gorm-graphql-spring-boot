package neo.script.gorm.portal.domain.pt.ds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'dsName')
@EqualsAndHashCode(includes = 'id')
class PortletDs {
    String id
    String dsName
    String type

    Date dateCreated
    Date lastUpdated

    static graphql = true
}
