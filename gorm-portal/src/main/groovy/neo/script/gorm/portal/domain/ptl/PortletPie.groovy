package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@InitializeDomian(profiles = 'dev', depends = Portal)
class PortletPie extends Portlet {

    String category

    static graphql = true

    static initList = [
            new PortletPie('各支行存款占比饼图', Portal.PERSONAL_PORTAL, 'PortletPie', 'year'),
            new PortletPie('贷款发放按月分析', Portal.PERSONAL_PORTAL, 'PortletRise', 'year'),
            new PortletPie('利率曲线', Portal.PERSONAL_PORTAL, 'PortletLine', 'year'),
            new PortletPie('存款按月分析', Portal.PERSONAL_PORTAL, 'PortletRise', 'year'),
            new PortletPie('经济资本按月分析', Portal.PERSONAL_PORTAL, 'PortletRise', 'year'),
            new PortletPie('客户经理业绩雷达图', Portal.PERSONAL_PORTAL, 'PortletRadar', 'year')
    ]
}
