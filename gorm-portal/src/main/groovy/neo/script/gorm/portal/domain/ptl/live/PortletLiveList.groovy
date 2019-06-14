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
    String titleFields
    String cateField
    String dateField
    String iconField
    String rowKey = 'ID'
    String fromDateFormat = 'YYYY-MM-DD HH:mm:ss'
    String toDateFormat = 'YYYY-MM-DD'

    static mapping = {
        liveQuery fetch: 'join', lazy: false
    }
    static constraints = {
        cateField nullable: true
        iconField nullable: true
        dateField nullable: true
        extraLink nullable: true
        fromDateFormat nullable: true
        toDateFormat nullable: true
    }
    static graphql = true

    static USER_LINK_LIST = new PortletLiveList('通讯录', 'PortletLiveList', LiveQuery.USER_LINK, "Name,UserID", 'Grade', 'ChgPwdTime')
    static initList = [USER_LINK_LIST]
}
