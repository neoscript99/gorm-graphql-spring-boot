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
@InitializeDomian(profiles = 'dev', depends = LiveQuery)
class PortletLiveList extends Portlet {
    LiveQuery liveQuery
    String titleField
    String cateField
    String dateField
    String iconField
    String fromDateFormat
    String toDateFormat

    static mapping = {
        liveQuery fetch: 'join', lazy: false
    }
    static constraints = {
        cateField nullable: true
        iconField nullable: true
        dateField nullable: true
        fromDateFormat nullable: true
        toDateFormat nullable: true
    }
    static graphql = true

    static DEMO_LIST = new PortletLiveList('员工列表', 'PortletLiveList', LiveQuery.DEMO_QUERY, "Name", 'Grade', 'LastLogin')
    static initList = [DEMO_LIST]
}
