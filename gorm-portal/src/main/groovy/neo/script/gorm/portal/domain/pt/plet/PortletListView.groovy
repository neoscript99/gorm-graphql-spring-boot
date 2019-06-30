package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = LivebosQuery)
class PortletListView extends Portlet {
    String titleFields
    String cateField
    String dateField
    String extraLink
    String titleLink
    String iconField
    String rowKey = 'ID'
    String fromDateFormat = 'YYYY-MM-DD HH:mm:ss'
    String toDateFormat = 'YYYY-MM-DD'

    static constraints = {
        cateField nullable: true
        iconField nullable: true
        dateField nullable: true
        extraLink nullable: true, maxSize: 512
        titleLink nullable: true, maxSize: 512
        fromDateFormat nullable: true
        toDateFormat nullable: true
    }
    static graphql = true

    static USER_LINK_LIST = new PortletListView('通讯录', 'PortletListView', LivebosQuery.USER_LINK,
            "Name,UserID", 'Grade', 'ChgPwdTime',
            "$LivebosServer.DEMO_SERVER.serverRoot/UIProcessor?Table=tUserLink",
            "$LivebosServer.DEMO_SERVER.serverRoot/UIProcessor?Table=tUserLink")
    static initList = [USER_LINK_LIST]
}
