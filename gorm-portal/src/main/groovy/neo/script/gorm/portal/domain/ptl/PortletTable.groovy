package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = [PortalCol, PortalDbQuery])
class PortletTable extends Portlet {

    PortalDbQuery dbQuery
    String columns
    String rowKey

    static mapping = {
        dbQuery fetch: 'join', lazy: false
    }

    static constraints = {
        columns maxSize: 512
    }
    static graphql

    static DEMO_TABLE1 = new PortletTable('员工列表', 'PortletTable', PortalDbQuery.EMP_LIST,
            """[{"title": "员工号", "dataIndex": "EMPNO", "sorter": true }, 
                                 {"title": "员工名", "dataIndex": "ENAME" },
                                 {"title": "入职日期", "dataIndex": "HIREDATE" }
                            ]""", 'EMPNO'
    )
    static DEMO_TABLE2 = new PortletTable('部门列表', 'PortletTable', PortalDbQuery.DEPT_LIST,
            """[{"title": "部门号", "dataIndex": "DEPTNO" }, 
                                 {"title": "部门名", "dataIndex": "DNAME" },
                                 {"title": "办公地址", "dataIndex": "LOC" }
                             ]""", 'DEPTNO'
    )
    static initList = [DEMO_TABLE1, DEMO_TABLE2]
}
