package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includePackage = false, includes = 'dbName, id')
@EqualsAndHashCode(includes = 'id')
class RdbServer {
    String id
    String dbName
    String driverClassName
    String url
    String username
    String password
    String testSql

    Date dateCreated
    Date lastUpdated

    static constraints = {
        driverClassName maxSize: 256
        url maxSize: 256
        testSql maxSize: 256
    }
    static graphql = true
}
