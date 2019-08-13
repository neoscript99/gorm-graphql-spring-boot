package ns.gflex.services

import groovy.xml.MarkupBuilder
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.service.*
import ns.gflex.services.base.GFlexService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * Generate menu for user
 * @since Dec 14, 2010
 * @author wangchu
 */
@Service
class FlexMenuService extends GFlexService {
    @Autowired
    MenuService menuService
    @Autowired
    UserService userService
    @Autowired
    RoleService roleService

    /**
     * @return menu xml
     */
    String getUserTree() {
        return generateXML(menuService.getUserTree(sessionUser))
    }

    String getRoleTree(def roleId) {
        return generateXML(menuService.getRoleTree(roleService.get(roleId)))
    }

    String getFullTree() {
        return generateXML(menuService.getFullTree())
    }

    protected String generateXML(MenuNode rootNode) {
        StringWriter writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)
        builder.root {
            rootNode.subMenus.each {
                generateTree(it, builder)
            }
        }
        String xmlStr = writer.toString()
        log.debug xmlStr
        return xmlStr
    }

    private generateTree(MenuNode menuNode, MarkupBuilder builder) {
        Menu menu = menuNode.menu
        builder.node(id: menu.id, label: menu.label, app: menu.app, isBranch: !menu.app) {
            menuNode.subMenus.each {
                generateTree(it, builder)
            }
        }
    }

    @Override
    public Class getDomainClass() {
        return Menu.class;
    }
}
