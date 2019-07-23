package ns.gflex.services

import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.service.RoleMenuService
import ns.gflex.services.base.GFlexService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;

/**
 * Functions
 * @since Feb 12, 2011
 * @author wangchu
 */
@Service
class FlexRoleService extends GFlexService {
    @Autowired
    RoleMenuService roleMenuService
    @Autowired
    FlexMenuService menuService

    String addRoleMenus(String roleId, String[] menuIds) {
        roleMenuService.addRoleMenus(roleId, menuIds)
        generalRepository.flush()
        menuService.getRoleTree(roleId)
    }

    String deleteRoleMenus(String roleId, String[] menuIds) {
        roleMenuService.deleteRoleMenus(roleId, menuIds)
        generalRepository.flush()
        menuService.getRoleTree(roleId)
    }

    @Override
    public Class getDomainClass() {
        Role.class;
    }
}
