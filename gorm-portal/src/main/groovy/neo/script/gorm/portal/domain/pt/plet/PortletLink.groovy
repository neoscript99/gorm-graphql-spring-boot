package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.PortalCol
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = PortalCol)
class PortletLink extends Portlet {

    String linkUrl
    String imageUrl

    static constraints = {
        imageUrl nullable: true, maxSize: 256
    }
    static graphql = GraphQLMapping.build {
        description('链接组件')
        property('linkUrl') { description('链接地址') }
        property('imageUrl') { description('链接图片') }
    }

    static DEMO_LINK1 = new PortletLink('OA系统', 'PortletLink', null, 'http://www.bing.com')
    static DEMO_LINK2 = new PortletLink('绩效考核系统', 'PortletLink', null, 'http://www.qq.com')
    static DEMO_LINK3 = new PortletLink('信贷管理系统', 'PortletLink', null, 'http://www.z.cn')

    static initList = [DEMO_LINK1, DEMO_LINK2, DEMO_LINK3]
}
