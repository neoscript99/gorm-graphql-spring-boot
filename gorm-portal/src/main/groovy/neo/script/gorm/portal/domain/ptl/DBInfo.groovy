package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@ToString(includePackage = false, includes = 'dbName, id')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class DBInfo {
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
        driverClassName maxSize: 128
        url maxSize: 128
    }

    static DBInfo SCOTT = new DBInfo([
            dbName         : 'OA数据库',
            driverClassName: 'oracle.jdbc.OracleDriver',
            url            : 'jdbc:oracle:thin:@114.115.153.164:1521:orcl',
            username       : 'scott',
            password       : 'tiger',
            testSql        : 'select 1 from dual'
    ])

    static initList = [SCOTT]
}
