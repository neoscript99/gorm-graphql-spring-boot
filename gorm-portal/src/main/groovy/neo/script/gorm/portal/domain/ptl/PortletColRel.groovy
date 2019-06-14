package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.ptl.live.PortletLiveList
import neo.script.gorm.portal.domain.ptl.live.PortletLiveTab

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [PortalCol, PortletLink, PortletTable, PortletPie, PortletLiveList, PortletLiveTab])
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
    static constraints = {
        col unique: 'portlet'
    }
    static graphql = true
    static initList = [
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK1, 1),
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK2, 2),
            new PortletColRel(PortalCol.DEMO_ROW1_COL1, PortletLink.DEMO_LINK3, 3),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE1, 1),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE2, 2),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE3, 3),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE4, 4),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE5, 5),
            new PortletColRel(PortalCol.DEMO_ROW2_COL2, PortletPie.DEMO_PIE6, 6),
            new PortletColRel(PortalCol.DEMO_ROW2_COL1, PortletTable.DEMO_TABLE1, 6),
            new PortletColRel(PortalCol.DEMO_ROW2_COL3, PortletTable.DEMO_TABLE2, 6),
    ]
}
