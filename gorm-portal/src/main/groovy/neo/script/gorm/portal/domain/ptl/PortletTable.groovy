package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'listName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [Portal, DBQuery])
class PortletTable extends Portlet {

    DBQuery dbQuery
    String columns
    String rowKey

    static mapping = {
        dbQuery fetch: 'join', lazy: false
    }

    static constraints = {
        columns maxSize: 512
    }
    static graphql

    static initList = [
            new PortletTable('员工列表', Portal.PERSONAL_PORTAL, 'PortletTable', DBQuery.EMP_LIST,
                    """[{"title": "员工号", "dataIndex": "EMPNO", "sorter": true }, 
                                 {"title": "员工名", "dataIndex": "ENAME" },
                                 {"title": "入职日期", "dataIndex": "HIREDATE" }
                            ]""",'EMPNO'
            ),
            new PortletTable('部门列表', Portal.PERSONAL_PORTAL, 'PortletTable', DBQuery.DEPT_LIST,
                    """[{"title": "部门号", "dataIndex": "DEPTNO" }, 
                                 {"title": "部门名", "dataIndex": "DNAME" },
                                 {"title": "办公地址", "dataIndex": "LOC" }
                             ]""",'DEPTNO'
            ),
    ]
}
