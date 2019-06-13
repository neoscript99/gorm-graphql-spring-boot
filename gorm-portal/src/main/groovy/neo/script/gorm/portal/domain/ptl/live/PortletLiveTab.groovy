package neo.script.gorm.portal.domain.ptl.live

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.ptl.Portlet

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
/**
 * 一个TAB包含多个List
 */
class PortletLiveTab extends Portlet {

    static mapping = {
    }
    static constraints = {
    }
    static graphql = true
    static DEMO_TAB = new PortletLiveTab('通知公告', 'PortletLiveTab')
    static initList = [DEMO_TAB]
}
