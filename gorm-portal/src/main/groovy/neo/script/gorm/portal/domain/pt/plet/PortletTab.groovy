package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
/**
 * 一个TAB包含多个List
 */
class PortletTab extends Portlet {

    static mapping = {
    }
    static constraints = {
    }
    static graphql = true
    static DEMO_TAB = new PortletTab('通知公告', 'PortletTab')
    static initList = [DEMO_TAB]
}
