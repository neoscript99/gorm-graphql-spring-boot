package neo.script.gorm.general.service

import groovy.xml.MarkupBuilder
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.domain.sys.UserRole
import org.springframework.stereotype.Service

@Service
/**
 *
 */
class MenuService extends AbstractService<Menu>{

	/**
	 * @return menu xml
	 */
	String getUserTree(){
		Set dispalyMenus=new HashSet()
		UserRole.findAllByUser(User.findByAccount(sessionAccount)).each{ userRole ->
			RoleMenu.findAllByRole(userRole.role).each { roleMenu->
				dispalyMenus+=getGenealogy(roleMenu.menu)
			}
		}
		return generateXML(dispalyMenus)
	}

	String getRoleTree(def roleId){
		Set dispalyMenus=new HashSet()
		list([role:[idEq:[roleId]]],RoleMenu).each {roleMenu->
			dispalyMenus+=getGenealogy(roleMenu.menu)
		}
		return generateXML(dispalyMenus)
	}

	String getFullTree(){
		Set dispalyMenus=new HashSet(list())
		return generateXML(dispalyMenus)
	}

	protected String generateXML(Set dispalyMenus){
		StringWriter writer = new StringWriter()
		MarkupBuilder builder = new MarkupBuilder(writer)
		builder.root {
			dispalyMenus.findAll{!it.parentId}.sort().each {
				generateTree(it,dispalyMenus,builder)
			}
		}
		String xmlStr = writer.toString()
		log.debug xmlStr
		return xmlStr
	}

	private List getGenealogy(Menu m){
		List list = [m]
		while(m.parentId)
			list<<(m=Menu.get(m.parentId))
		return list
	}

	private generateTree(Menu menu,Set dispalyMenus,MarkupBuilder builder){
		builder.node(id:menu.id,label:menu.label,app:menu.app,isBranch:!menu.app){
			dispalyMenus.findAll { it.parentId==menu.id }.sort().each{
				generateTree(it,dispalyMenus,builder)
			}
		}
	}
}
