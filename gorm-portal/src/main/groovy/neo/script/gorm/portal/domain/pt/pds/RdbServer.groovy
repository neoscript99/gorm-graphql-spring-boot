package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@ToString(includePackage = false, includes = 'dbName, id')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
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
    static graphql = GraphQLMapping.build {
        description('关系数据库服务端')
    }

    static RdbServer DEMO_ORA = new RdbServer([
            dbName         : 'OA数据库',
            driverClassName: 'oracle.jdbc.OracleDriver',
            url            : 'jdbc:oracle:thin:@114.115.153.164:1521:orcl',
            username       : 'scott',
            password       : 'tiger',
            testSql        : 'select 1 from dual'
    ])
    static RdbServer DEMO_DB2 = new RdbServer([
            dbName         : 'DB2数据库',
            driverClassName: 'com.ibm.db2.jcc.DB2Driver',
            url            : 'jdbc:db2://114.115.146.122:60000/DSC:currentSchema=LIVEBOS;',
            username       : 'tymh',
            password       : '000000',
            testSql        : 'SELECT current date FROM sysibm.sysdummy1'
    ])
    static initList = [DEMO_ORA, DEMO_DB2]
}
