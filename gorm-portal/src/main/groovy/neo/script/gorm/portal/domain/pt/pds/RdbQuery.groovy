package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = RdbServer)
class RdbQuery extends PortletDataSource {

    RdbServer db
    String sql

    static mapping = {
        db fetch: 'join', lazy: false
    }
    static constraints = {
        sql maxSize: 2048
    }
    static graphql = true

    static RdbQuery DEMO_ORA_EMP_LIST = new RdbQuery('员工列表', 'RdbQuery', RdbServer.DEMO_ORA,
            '''select empno,ename,job,mgr,to_char(hiredate,'yyyy-mm-dd') hiredate,sal,comm,deptno from emp 
where to_char(hiredate,'yyyy-mm-dd') < '${yesterday}' and ename <> '${user}' ''')
    static RdbQuery DEMO_ORA_DEPT_LIST = new RdbQuery('部门列表', 'RdbQuery', RdbServer.DEMO_ORA,
            'select deptno, dname, loc from dept')


    static RdbQuery DEMO_DB2_MENU_LIST = new RdbQuery('菜单列表', 'RdbQuery', RdbServer.DEMO_DB2,
            '''select t.* from tmenu t FETCH FIRST 100 ROWS ONLY''')

    static initList = [DEMO_ORA_EMP_LIST, DEMO_ORA_DEPT_LIST, DEMO_DB2_MENU_LIST]
}
