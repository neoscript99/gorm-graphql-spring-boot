package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.pds.RdbQuery
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = RdbQuery)
class PortletTable extends Portlet {

    String columns
    String rowKey
    Integer pageSize = 6

    static constraints = {
        columns maxSize: 2048
    }
    static graphql = GraphQLMapping.build {
        description('表格组件')
        property('columns') { description('列定义') }
        property('rowKey') { description('ID字段') }
        property('pageSize') { description('分页大小') }
    }

    static DEMO_TABLE1 = new PortletTable('员工列表', 'PortletTable', RdbQuery.DEMO_ORA_EMP_LIST,
            null, """[{"title": "员工号", "dataIndex": "EMPNO", "sorter": true }, 
                                 {"title": "员工名", "dataIndex": "ENAME" },
                                 {"title": "入职日期", "dataIndex": "HIREDATE" }
                            ]""", 'EMPNO'
    )
    static DEMO_TABLE2 = new PortletTable('部门列表', 'PortletTable', RdbQuery.DEMO_ORA_DEPT_LIST,
            null, """[{"title": "部门号", "dataIndex": "DEPTNO" }, 
                                 {"title": "部门名", "dataIndex": "DNAME" },
                                 {"title": "办公地址", "dataIndex": "LOC" }
                             ]""", 'DEPTNO'
    )
    static DEMO_TABLE3 = new PortletTable('菜单列表', 'PortletTable', RdbQuery.DEMO_DB2_MENU_LIST,
            null, """[{"title": "菜单名", "dataIndex": "TITLE", "sorter": true }, 
{"title": "菜单级别", "dataIndex": "LEVEL" },
{"title": "实体对象名", "dataIndex": "TABLENAME" }]""", 'ID'
    )
    static initList = [DEMO_TABLE1, DEMO_TABLE2, DEMO_TABLE3]
}
