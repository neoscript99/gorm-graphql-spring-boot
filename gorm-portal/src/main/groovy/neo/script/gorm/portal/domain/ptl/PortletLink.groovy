package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'label')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = Portal)
class PortletLink extends Portlet {

    String linkUrl
    String imageUrl

    static constraints = {
        imageUrl nullable: true, maxSize: 256
    }
    static graphql = true

    static initList = [
            new PortletLink('金蝶OA系统EAS', Portal.PERSONAL_PORTAL, 'PortletLink', 'http://www.bing.com'),
            new PortletLink('新绩效考核系统', Portal.PERSONAL_PORTAL, 'PortletLink', 'http://www.qq.com'),
            new PortletLink('信贷管理系统', Portal.PERSONAL_PORTAL, 'PortletLink', 'http://www.z.cn')
    ]
}
