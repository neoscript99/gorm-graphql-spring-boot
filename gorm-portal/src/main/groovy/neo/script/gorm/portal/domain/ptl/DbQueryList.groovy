package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@ToString(includePackage = false, includes = 'listName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = PortalDb)
class DbQueryList {
    String id
    String listName
    PortalDb db
    String queryString
    String rowTemplate

    Date dateCreated
    Date lastUpdated

    static mapping = {
        db fetch: 'join', lazy: false
    }

    static constraints = {
        queryString maxSize: 512
        rowTemplate maxSize: 512
    }

    static initList = [new DbQueryList(
            [listName   : 'OA待办',
             db         : PortalDb.SCOTT,
             queryString: 'select empno,ename,job,mgr,hiredate,sal,comm,deptno from emp',
             rowTemplate: '${ename}'])]
}
