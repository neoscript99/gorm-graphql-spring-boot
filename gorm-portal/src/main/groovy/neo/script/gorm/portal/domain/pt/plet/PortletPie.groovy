package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.PortalCol
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = PortalCol)
class PortletPie extends Portlet {

    String category

    static graphql = GraphQLMapping.build {
        description('饼图组件')
        property('category') { description('序列') }
    }

    static DEMO_PIE1 = new PortletPie('各支行存款占比饼图', 'PortletPie', null, null, 'year')
    static DEMO_PIE2 = new PortletPie('贷款发放按月分析', 'PortletRise', null, null, 'year')
    static initList = [DEMO_PIE1, DEMO_PIE2]
}
