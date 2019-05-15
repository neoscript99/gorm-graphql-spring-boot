package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = DBInfo)
class DBQuery {
    String id

    DBInfo db
    String queryName
    String sql

    Date dateCreated
    Date lastUpdated

    static mapping = {
        db fetch: 'join', lazy: false
    }
    static constraints = {
        sql maxSize: 1024
    }

    static DBQuery EMP_LIST = new DBQuery(DBInfo.SCOTT, '员工列表', 'select empno,ename,job,mgr,hiredate,sal,comm,deptno from emp')
    static DBQuery DEPT_LIST = new DBQuery(DBInfo.SCOTT, '部门列表', 'select deptno, dname, loc from dept')
    static initList = [EMP_LIST, DEPT_LIST]
}
