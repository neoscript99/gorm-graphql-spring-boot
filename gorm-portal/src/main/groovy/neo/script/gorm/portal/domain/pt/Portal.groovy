package neo.script.gorm.portal.domain.pt


import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class Portal {
    String id
    String portalName
    String portalCode
    String portalIcon
    Integer seq
    Boolean enabled = true

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
    }
    static graphql = GraphQLMapping.build {
        description('门户')
        property('portalName') { description('门户名') }
        property('portalCode') { description('门户代码') }
        property('portalIcon') { description('门户图标') }
        property('seq') { description('门户排序号') }
    }

    static Portal PERSONAL_PORTAL = new Portal('个人门户', 'personal', 'user', 1)
    static initList = [
            PERSONAL_PORTAL,
            new Portal('仪表盘', 'pie-chart', 'pie-chart', 2),
            new Portal('流程中心', 'radar-chart', 'radar-chart', 3)
    ]
}
