package neo.script.gorm.portal.config

import groovy.util.logging.Slf4j
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.initializer.AbstractDataInitializer
import neo.script.gorm.general.initializer.DataInitializer
import neo.script.gorm.portal.domain.ptl.Portal
import neo.script.gorm.portal.domain.ptl.PortalCol
import neo.script.gorm.portal.domain.ptl.PortalRow
import neo.script.gorm.portal.domain.ptl.PortalRowRel
import neo.script.gorm.portal.domain.ptl.PortletColRel
import neo.script.gorm.portal.domain.ptl.live.PortletLiveList
import neo.script.gorm.portal.util.Consts
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class PortalLuqiaoInitializer extends AbstractDataInitializer implements DataInitializer {
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


        [new PortletColRel(lqRow1Col1, PortletLiveList.USER_LINK_LIST, 6),
         new PortletColRel(lqRow1Col2, PortletLiveList.USER_LINK_LIST, 6),
         new PortletColRel(lqRow1Col3, PortletLiveList.USER_LINK_LIST, 6),
        ].each {
            save(it)
        }
    }
}
