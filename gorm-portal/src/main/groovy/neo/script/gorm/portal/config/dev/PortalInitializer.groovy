package neo.script.gorm.portal.config.dev

import groovy.util.logging.Slf4j
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.initializer.AbstractDataInitializer
import neo.script.gorm.general.initializer.DataInitializer
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class PortalInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        return generalRepository.list(Menu, [eq: [['app', 'PortalManage']]])
    }

    @Override
    void doInit() {
        initRoleMenu()
    }

    void initRoleMenu() {
        def rootId = generalRepository.findFirst(Menu, [isNull: ['parentId']]).id
        Menu portalFolder = save(new Menu(label: '门户管理', seq: 30, parentId: rootId))
        [
                save(new Menu(label: '门户管理', app: 'PortalManage', seq: 10, parentId: portalFolder.id)),
                save(new Menu(label: '控件管理', app: 'PortletManage', seq: 20, parentId: portalFolder.id)),
                save(new Menu(label: '数据源管理', app: 'PortalDbManage', seq: 30, parentId: portalFolder.id)),
                save(new Menu(label: '查询管理', app: 'PortalDbQueryManage', seq: 40, parentId: portalFolder.id)),
        ].each {
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
    }
}
