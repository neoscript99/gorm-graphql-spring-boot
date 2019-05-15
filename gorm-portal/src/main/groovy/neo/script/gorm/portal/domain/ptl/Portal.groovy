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
    String portalIcon
    Boolean enabled = true

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
    }
    static graphql = true

    static Portal PERSONAL_PORTAL = new Portal('个人门户', 'user')
    static initList = [
            PERSONAL_PORTAL,
            new Portal('仪表盘', 'pie-chart'),
            new Portal('流程中心', 'radar-chart')
    ]
}
