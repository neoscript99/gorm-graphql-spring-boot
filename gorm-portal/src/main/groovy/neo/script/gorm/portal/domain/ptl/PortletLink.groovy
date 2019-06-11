package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = PortalCol)
class PortletLink extends Portlet {

    String linkUrl
    String imageUrl

    static constraints = {
        imageUrl nullable: true, maxSize: 256
    }
    static graphql = true

    static DEMO_LINK1 = new PortletLink('金蝶OA系统EAS', 'PortletLink', 'http://www.bing.com')
    static DEMO_LINK2 = new PortletLink('新绩效考核系统', 'PortletLink', 'http://www.qq.com')
    static DEMO_LINK3 = new PortletLink('信贷管理系统', 'PortletLink', 'http://www.z.cn')

    static initList = [DEMO_LINK1, DEMO_LINK2, DEMO_LINK3]
}
