package neo.script.gorm.portal.config

import groovy.util.logging.Slf4j
import neo.script.gorm.general.initializer.AbstractDataInitializer
import neo.script.gorm.general.initializer.DataInitializer
import neo.script.gorm.portal.domain.pt.Portal
import neo.script.gorm.portal.domain.pt.PortalCol
import neo.script.gorm.portal.domain.pt.PortalRow
import neo.script.gorm.portal.domain.pt.PortalRowRel
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
import neo.script.gorm.portal.domain.pt.plet.PortletCalendar
import neo.script.gorm.portal.domain.pt.plet.PortletColRel
import neo.script.gorm.portal.domain.pt.plet.PortletCustomize
import neo.script.gorm.portal.domain.pt.plet.PortletListView
import neo.script.gorm.portal.domain.pt.plet.PortletTab
import neo.script.gorm.portal.domain.pt.plet.PortletTabRel
import neo.script.gorm.portal.util.Consts
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class LuqiaoInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return generalRepository.list(Portal, [eq: [['portalCode', 'luqiao']]])
    }

    @Override
    void doInit() {
        initLuqiaoPortal()
    }

    def initLuqiaoPortal() {
        Portal lqPortal = save(new Portal('路桥门户', 'luqiao', 'user', 4))
        PortalRow lqRow1 = save(new PortalRow('LUQIAO_ROW1'))
        save(new PortalRowRel(lqPortal, lqRow1, 1))

        PortalCol lqRow1Col1 = save(new PortalCol('LUQIAO_ROW1_COL1', lqRow1, 1, 6, Consts.STYLE_FLEX_COL))
        PortalCol lqRow1Col2 = save(new PortalCol('LUQIAO_ROW1_COL2', lqRow1, 2, 12, Consts.STYLE_FLEX_COL))
        PortalCol lqRow1Col3 = save(new PortalCol('LUQIAO_ROW1_COL3', lqRow1, 3, 6, Consts.STYLE_FLEX_COL))


        def luqiaoServer = save(new LivebosServer('LuqiaoServer',
                'http://114.115.153.164:7070',
                'rest', '000000'))
        def userLinkQuery = save(new LivebosQuery('通讯录', 'LivebosQuery', luqiaoServer,
                'tUserLink', '', 'DISPLAY', 1, 1000))

        def newsClass1Query = save(new LivebosQuery('公司要闻', 'LivebosQuery', luqiaoServer,
                'T_SYS_NewsInfo', 'FClass = 1', 'DISPLAY'))
        def newsClass2Query = save(new LivebosQuery('纪要新闻', 'LivebosQuery', luqiaoServer,
                'T_SYS_NewsInfo', 'FClass = 2', 'DISPLAY'))

        def newsClass1ListView = save(new PortletListView('公司要闻', 'PortletListView',
                newsClass1Query, "FTitle", null, 'PubTime',
                "$luqiaoServer.serverRoot/UIProcessor?Table=VMA_SYS_NewsInfo&ParamAction=true&PClass=1",
                "$luqiaoServer.serverRoot/UIProcessor?Table=BPM_News&UIMode=PLUGIN.UIMODE.NEWS.DETAIL&ID={ID}"))
        def newsClass2ListView = save(new PortletListView('纪要新闻', 'PortletListView',
                newsClass2Query, "FTitle", null, 'PubTime',
                "$luqiaoServer.serverRoot/UIProcessor?Table=VMA_SYS_NewsInfo&ParamAction=true&PClass=2",
                "$luqiaoServer.serverRoot/UIProcessor?Table=BPM_News&UIMode=PLUGIN.UIMODE.NEWS.DETAIL&ID={ID}"))

        def newsTab = save(new PortletTab('新闻中心', 'PortletTab'))
        [
                new PortletTabRel(newsTab, newsClass1ListView, 1),
                new PortletTabRel(newsTab, newsClass2ListView, 2),
        ].each { save(it) }

        def contactSearch = save(new PortletCustomize('通讯录搜索框', 'ContactSearch', userLinkQuery))

        def systemListQuery = save(new LivebosQuery('系统信息', 'LivebosQuery', luqiaoServer,
                'VA_ESB_SysInfo', '', 'DISPLAY', 1, 10))
        def systemList = save(new PortletCustomize('常用系统', 'SystemList', systemListQuery))

        def calendarCard = save(new PortletCalendar('日程安排', 'PortletCalendar'))

        [new PortletColRel(lqRow1Col1, newsClass2ListView, 1),
         new PortletColRel(lqRow1Col1, calendarCard, 2),
         new PortletColRel(lqRow1Col2, newsClass1ListView, 1),
         new PortletColRel(lqRow1Col2, newsTab, 2),
         new PortletColRel(lqRow1Col3, contactSearch, 1),
         new PortletColRel(lqRow1Col3, systemList, 2),
        ].each {
            save(it)
        }

    }
}
