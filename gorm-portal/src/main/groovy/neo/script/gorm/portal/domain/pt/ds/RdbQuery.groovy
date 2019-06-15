package neo.script.gorm.portal.domain.pt.ds

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
class RdbQuery  extends PortletDs {

    RdbServer db
    String sql

    static mapping = {
        db fetch: 'join', lazy: false
    }
    static constraints = {
        sql maxSize: 1024
    }
    static graphql = true

    static RdbQuery EMP_LIST = new RdbQuery('员工列表', 'RdbQuery', RdbServer.SCOTT,
            "select empno,ename,job,mgr,to_char(hiredate,'yyyy-mm-dd') hiredate,sal,comm,deptno from emp")
    static RdbQuery DEPT_LIST = new RdbQuery('部门列表', 'RdbQuery', RdbServer.SCOTT,
            'select deptno, dname, loc from dept')
    static initList = [EMP_LIST, DEPT_LIST]
}
