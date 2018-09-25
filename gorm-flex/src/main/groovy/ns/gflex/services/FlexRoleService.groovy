package ns.gflex.services

import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.RoleMenu
import ns.gflex.services.base.GFlexService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;

/**
 * Functions
 * @since Feb 12, 2011
 * @author wangchu
 */
@Service
class FlexRoleService extends GFlexService{
	@Autowired
	FlexMenuService menuService
	
	String addRoleMenus(Long roleId,Long[] menuIds){
		log.debug "addRoleMenu $roleId $menuIds"
		menuIds.each {menuId ->
			Menu mm = Menu.get(menuId)
			//app不为空的需要保存
			if(mm.app)
				if(!count([role:[idEq:[roleId]],menu:[idEq:[menuId]]],RoleMenu))
					saving(new RoleMenu(role:Role.get(roleId),menu:mm))
		}
		menuService.getRoleTree(roleId)
	}	
	String deleteRoleMenus(Long roleId,Long[] menuIds){
		log.debug "deleteRoleMenu $roleId $menuIds"
		menuIds.each {menuId ->
			list([role:[idEq:[roleId]],menu:[idEq:[menuId]]],RoleMenu)*.delete()
		}
		menuService.getRoleTree(roleId)
	}	
	@Override
	public Class getDomainClass() {
		Role.class;
	}
}
