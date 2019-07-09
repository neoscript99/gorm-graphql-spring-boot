package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
/**
 * 一个TAB包含多个List
 */
class PortletTab extends Portlet {

    static mapping = {
    }
    static constraints = {
    }
    static graphql = GraphQLMapping.build {
        description('TAB页组件（可包含多个普通组件）')
    }
}
