package neo.script.gorm.portal.domain.pt

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [Portal, PortalRow])
class PortalRowRel {
    String id
    Portal portal
    PortalRow row
    Integer rowOrder
    Date dateCreated
    Date lastUpdated

    static mapping = {
        portal fetch: 'join', lazy: false
        row fetch: 'join', lazy: false
    }
    static constraints = {
    }
    static graphql = true
    static initList = [
            new PortalRowRel(Portal.PERSONAL_PORTAL, PortalRow.DEMO_ROW1, 1),
            new PortalRowRel(Portal.PERSONAL_PORTAL, PortalRow.DEMO_ROW2, 2)
    ]
}
