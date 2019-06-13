package neo.script.gorm.portal.domain.ptl.live

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [PortletLiveTab, PortletLiveList])
class PortletLiveTabListRel {
    String id

    PortletLiveTab tab
    PortletLiveList list
    Integer listOrder = 0

    Date dateCreated
    Date lastUpdated

    static mapping = {
        tab fetch: 'join', lazy: false
        list fetch: 'join', lazy: false
    }
    static constraints = {
    }
    static graphql = true

    static PortletLiveTabListRel = new PortletLiveTabListRel(PortletLiveTab.DEMO_TAB, PortletLiveList.DEMO_LIST, 1)
    static initList = []
}
