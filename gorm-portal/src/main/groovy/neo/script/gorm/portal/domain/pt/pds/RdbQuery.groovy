package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
class RdbQuery  extends PortletDataSource {

    RdbServer db
    String sql

    static mapping = {
        db fetch: 'join', lazy: false
    }
    static constraints = {
        sql maxSize: 2048
    }
    static graphql = true
}
