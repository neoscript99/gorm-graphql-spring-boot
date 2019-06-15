package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [PortletTab, PortletListView])
class PortletTabListRel {
    String id

    PortletTab tab
    PortletListView list
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

    static PortletLiveTabListRel = new PortletTabListRel(PortletTab.DEMO_TAB, PortletListView.USER_LINK_LIST, 1)
    static initList = []
}
