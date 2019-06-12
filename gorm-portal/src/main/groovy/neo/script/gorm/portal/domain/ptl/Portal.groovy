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
    static graphql = true

    static Portal PERSONAL_PORTAL = new Portal('个人门户', 'personal', 'user', 1)
    static Portal LIVEBPM_PORTAL_DEMO = new Portal('LIVEBPM', 'livebpm', 'user', 4)
    static initList = [
            PERSONAL_PORTAL, LIVEBPM_PORTAL_DEMO,
            new Portal('仪表盘', 'pie-chart', 'pie-chart', 2),
            new Portal('流程中心', 'radar-chart', 'radar-chart', 3)
    ]
}
