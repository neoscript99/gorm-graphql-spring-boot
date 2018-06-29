package neo.script.gorm.general.config.data

import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.initializer.AbstractDataInitializer
import neo.script.gorm.general.initializer.DataInitializer
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import org.springframework.core.annotation.Order

/**
 * Created by Neo on 2017-08-22.
 */
@Order(100)
class MenuInitializer extends AbstractDataInitializer implements DataInitializer {

    @Override
    boolean isInited() {
        return (generalRepository.countAll(Menu) > 0)
    }

    void doInit() {

        def rootMenu = save(new Menu(label: 'Root',))

        initAdminMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
        initNormalUsersMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.NORMAL_USERS, menu: it))
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
        initPublicMenu(rootMenu.id).each {
            save(it)
            save(new RoleMenu(role: Role.PUBLIC, menu: it))
            save(new RoleMenu(role: Role.NORMAL_USERS, menu: it))
            save(new RoleMenu(role: Role.ADMINISTRATORS, menu: it))
        }
    }

    private List initAdminMenu(def rootId) {
        def sys = save(new Menu(label: '系统设置', seq: 90, parentId: rootId))

        [
                new Menu(label: '帐号管理', app: 'User', seq: 2, parentId: sys.id),
                new Menu(label: '角色管理', app: 'Role', seq: 1, parentId: sys.id),
                new Menu(label: '发布通知', app: 'Note', seq: 3, parentId: sys.id),
                new Menu(label: '参数维护', app: 'Param', seq: 4, parentId: sys.id)
        ]
    }

    private List initNormalUsersMenu(def rootId) {

        [
                new Menu(label: '用户设置', app: 'Profile', seq: 99, parentId: rootId)
        ]
    }

    private List initPublicMenu(def rootId) {
        [
                new Menu(label: '欢迎页面', app: 'Welcome', seq: 10, parentId: rootId)
        ]
    }
}
