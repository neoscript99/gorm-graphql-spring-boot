package neo.script.gorm.portal.config.dev

import groovy.util.logging.Slf4j
import neo.script.gorm.general.initializer.AbstractDataInitializer
import neo.script.gorm.general.initializer.DataInitializer
import neo.script.gorm.portal.domain.pt.Portal
import neo.script.gorm.portal.domain.pt.PortalCol
import neo.script.gorm.portal.domain.pt.PortalRow
import neo.script.gorm.portal.domain.pt.PortalRowRel
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
import neo.script.gorm.portal.domain.pt.pds.RdbQuery
import neo.script.gorm.portal.domain.pt.pds.RdbServer
import neo.script.gorm.portal.domain.pt.plet.*
import neo.script.gorm.portal.util.Consts
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class LivebosPortalInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return generalRepository.list(Portal, [eq: [['portalCode', 'livebos']]])
    }

    @Override
    void doInit() {
        initPortalRowCol()
    }


    void initPortalRowCol() {
        Portal lqPortal = save(new Portal('Livebos门户', 'livebos', 'bank', 0))
        PortalRow lqRow1 = save(new PortalRow('LUQIAO_ROW1'))
        save(new PortalRowRel(lqPortal, lqRow1, 1))

        PortalCol lqRow1Col1 = save(new PortalCol('LUQIAO_ROW1_COL1', lqRow1, 1, 6, Consts.STYLE_FLEX_COL))
        PortalCol lqRow1Col2 = save(new PortalCol('LUQIAO_ROW1_COL2', lqRow1, 2, 12, Consts.STYLE_FLEX_COL))
        PortalCol lqRow1Col3 = save(new PortalCol('LUQIAO_ROW1_COL3', lqRow1, 3, 6, Consts.STYLE_FLEX_COL))

        initLivebos(lqRow1Col1, lqRow1Col2, lqRow1Col3)
        initRdb(lqRow1Col1, lqRow1Col2, lqRow1Col3)
    }

    LivebosServer initLivebosServer() {
        return save(new LivebosServer('LuqiaoServer',
                'http://114.115.153.164:7070',
                'rest', '000000'))
    }

    def initLivebos(PortalCol lqRow1Col1, PortalCol lqRow1Col2, PortalCol lqRow1Col3) {
        def livebosServer = initLivebosServer()

        def contactQuery = save(new LivebosQuery('通讯录', 'LivebosQuery', livebosServer,
                'XUXB_TXL', '', 'DISPLAY', 1, 2000))
        def contactSearch = save(new PortletCustomize('通讯录搜索框', 'ContactSearch', contactQuery))


        LivebosQuery newsClass1Query = save(new LivebosQuery('纪要新闻', 'LivebosQuery', livebosServer,
                'XUXB_XWDT', 'Category = 1', 'DISPLAY'))
        LivebosQuery newsClass2Query = save(new LivebosQuery('公司要闻', 'LivebosQuery', livebosServer,
                'XUXB_XWDT', 'Category = 2', 'DISPLAY'))

        def newsClass1ListView = save(new PortletListView('纪要新闻', 'PortletListView',
                newsClass1Query, "Title", null, 'PubTime',
                "$livebosServer.serverRoot/UIProcessor?&Group=Category.1&UIMode=UIMODE.NEWSBOARD.VIEW&Table=XUXB_XWDT&Main=true",
                "$livebosServer.serverRoot/UIProcessor?ViewMode=UIMODE.NEWSBOARD.RECORD&&Table=XUXB_XWDT&WindowType=2&extWindow=true&PopupWin=false&NewWindow=true&WindowType=2&ID={ID}"))
        def newsClass2ListView = save(new PortletListView('公司要闻', 'PortletListView',
                newsClass2Query, "Title", null, 'PubTime',
                "$livebosServer.serverRoot/UIProcessor?&Group=Category.2&UIMode=UIMODE.NEWSBOARD.VIEW&Table=XUXB_XWDT&Main=true",
                "$livebosServer.serverRoot/UIProcessor?ViewMode=UIMODE.NEWSBOARD.RECORD&&Table=XUXB_XWDT&WindowType=2&extWindow=true&PopupWin=false&NewWindow=true&WindowType=2&ID={ID}"))

        def newsTab = save(new PortletTab('新闻中心', 'PortletTab'))
        [
                new PortletTabRel(newsTab, newsClass1ListView, 1),
                new PortletTabRel(newsTab, newsClass2ListView, 2),
        ].each { save(it) }


        def systemListQuery = save(new LivebosQuery('个人应用', 'LivebosQuery', livebosServer,
                'XUXB_GRYYK', '', 'DISPLAY', 1, 10))
        def systemList = save(new PortletCustomize('常用系统', 'SystemList', systemListQuery))

        def profitQuery = save(new LivebosQuery('业务报表', 'LivebosQuery', livebosServer,
                'PROFIT_DAILY', '', 'DISPLAY', 1, 100))

        def profitTable = save(new PortletTable('业务报表', 'PortletTable', profitQuery,
                """[{"title": "指标", "dataIndex": "PARAM", "sorter": true }, 
{"title": "数据日期", "dataIndex": "DATA_DATE" },
{"title": "余额", "dataIndex": "VAL", "align":"right" },
{"title": "比昨日", "dataIndex": "BZR", "align":"right" },
{"title": "比上月", "dataIndex": "BSY", "align":"right" },
{"title": "比年初", "dataIndex": "BNC", "align":"right" }]""", 'ID'
        ))
        def calendarCard = save(new PortletCalendar('日程安排', 'PortletCalendar'))

        [new PortletColRel(lqRow1Col1, newsClass2ListView, 1),
         new PortletColRel(lqRow1Col1, calendarCard, 2),
         new PortletColRel(lqRow1Col2, newsClass1ListView, 1),
         new PortletColRel(lqRow1Col2, newsTab, 2),
         new PortletColRel(lqRow1Col2, profitTable, 3),
         new PortletColRel(lqRow1Col3, contactSearch, 1),
         new PortletColRel(lqRow1Col3, systemList, 2),
        ].each {
            save(it)
        }
    }

    void initRdb(PortalCol lqRow1Col1, PortalCol lqRow1Col2, PortalCol lqRow1Col3) {
        def rdbServer = save(new RdbServer([
                dbName         : 'DB2数据库',
                driverClassName: 'com.ibm.db2.jcc.DB2Driver',
                url            : 'jdbc:db2://114.115.146.122:60000/DSC:currentSchema=LIVEBOS;',
                username       : 'tymh',
                password       : '000000',
                testSql        : 'SELECT current date FROM sysibm.sysdummy1'
        ]));

        def menuQuery = save(new RdbQuery('菜单列表', 'RdbQuery', rdbServer,
                '''select t.*
from tmenu t
    FETCH FIRST 100 ROWS ONLY'''))


        def menuTable = save(new PortletTable('菜单列表', 'PortletTable', menuQuery,
                """[{"title": "菜单名", "dataIndex": "TITLE", "sorter": true }, 
{"title": "菜单级别", "dataIndex": "LEVEL" },
{"title": "实体对象名", "dataIndex": "TABLENAME" }]""", 'ID'
        ))


        [
                new PortletColRel(lqRow1Col1, menuTable, 3),
        ].each {
            save(it)
        }
    }
}
