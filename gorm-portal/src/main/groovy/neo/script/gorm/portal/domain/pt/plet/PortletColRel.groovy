package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.PortalCol

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [PortalCol, PortletLink, PortletTable, PortletPie, PortletListView, PortletTab])
class PortletColRel {
    String id

    PortalCol col
    Portlet portlet
    Integer portletOrder

    Date dateCreated
    Date lastUpdated

    static mapping = {
        col fetch: 'join', lazy: false
        portlet fetch: 'join', lazy: false
    }
    static graphql = true
    static initList = [
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK1, 1),
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK2, 2),
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK3, 3),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE1, 1),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE2, 2)
    ]
}
