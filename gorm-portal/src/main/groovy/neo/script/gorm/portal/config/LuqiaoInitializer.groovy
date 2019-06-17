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
import neo.script.gorm.portal.domain.pt.plet.PortletColRel
import neo.script.gorm.portal.domain.pt.plet.PortletCustomize
import neo.script.gorm.portal.domain.pt.plet.PortletListView
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


        def luqiaoServer = save(new LivebosServer('路桥LivebosServer',
                'http://114.115.153.164:7070',
                'rest', '000000'))
        def userLinkQuery = save(new LivebosQuery('通讯录', 'LivebosQuery', luqiaoServer,
                'tUserLink', '', 'DISPLAY', 1, 1000))
        def userLinkListView = save(new PortletListView('通讯录', 'PortletListView',
                userLinkQuery, "Name,UserID", 'Grade', 'ChgPwdTime'))

        def contactSearch = save(new PortletCustomize('通讯录搜索框', 'ContactSearch', userLinkQuery))
        def contactSearch = save(new PortletCustomize('通讯录搜索框', 'SystemList', userLinkQuery))

        [new PortletColRel(lqRow1Col1, userLinkListView, 1),
         new PortletColRel(lqRow1Col2, userLinkListView, 1),
         new PortletColRel(lqRow1Col3, contactSearch, 1),
        ].each {
            save(it)
        }

    }
}
