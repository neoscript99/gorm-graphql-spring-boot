package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.PortalCol

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = PortalCol)
class PortletPie extends Portlet {

    String category

    static graphql = true

    static DEMO_PIE1 = new PortletPie('各支行存款占比饼图', 'PortletPie', 'year')
    static DEMO_PIE2 = new PortletPie('贷款发放按月分析', 'PortletRise', 'year')
    static DEMO_PIE3 = new PortletPie('利率曲线', 'PortletLine', 'year')
    static DEMO_PIE4 = new PortletPie('存款按月分析', 'PortletRise', 'year')
    static DEMO_PIE5 = new PortletPie('经济资本按月分析', 'PortletRise', 'year')
    static DEMO_PIE6 = new PortletPie('客户经理业绩雷达图', 'PortletRadar', 'year')
    static initList = [DEMO_PIE1, DEMO_PIE2, DEMO_PIE3, DEMO_PIE4, DEMO_PIE5, DEMO_PIE6]
}
