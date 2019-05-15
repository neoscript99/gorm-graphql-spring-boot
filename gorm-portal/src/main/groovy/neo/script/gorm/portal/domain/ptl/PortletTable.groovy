package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'listName')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [Portal, DBQuery])
class PortletTable extends Portlet {

    DBQuery dbQuery
    String columns

    static mapping = {
        dbQuery fetch: 'join', lazy: false
    }

    static constraints = {
        columns maxSize: 512, nullable: true
    }
    static graphql = GraphQLMapping.build {
        exclude('dbQuery')
    }

    static initList = [
            new PortletTable('员工列表', Portal.PERSONAL_PORTAL, 'PortletTable', DBQuery.EMP_LIST),
            new PortletTable('部门列表', Portal.PERSONAL_PORTAL, 'PortletTable', DBQuery.DEPT_LIST),
    ]
}
