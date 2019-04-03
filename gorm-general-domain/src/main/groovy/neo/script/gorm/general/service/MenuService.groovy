package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.RoleMenu
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.domain.sys.UserRole
import org.springframework.stereotype.Service

@Service
class MenuService extends AbstractService<Menu> {

    /**
     * 获取用户对用角色所具有的所有菜单，并分目录和排序
     * @return menu xml
     */
    Collection<MenuNode> getUserTree(User user) {
        return generateTree(UserRole.findAllByUser(user)*.role)
    }

    Collection<MenuNode> getRoleTree(Role role) {
        return generateTree(getMenuByRoles([role]))
    }

    Collection<MenuNode> getFullTree() {
        return generateTree(list())
    }

    protected Collection<Menu> getMenuByRoles(List<Role> roleList) {
        def roleMenus = new HashSet<Menu>()
        roleList.each { role ->
            roleMenus += RoleMenu.findAllByRole(role)*.menu
        }
        return roleMenus
    }

    protected Collection<MenuNode> generateTree(Collection<Menu> menuList) {
        def displayMenus = new HashSet<Menu>()
        menuList.each {
            displayMenus += getGenealogy(it)
        }
        def nodeList = new LinkedList<MenuNode>()
        displayMenus.findAll { !it.parentId }.sort().each {
            nodeList.add(new MenuNode(it, generateSubTree(it, displayMenus)))
        }
        return nodeList
    }

    private List getGenealogy(Menu m) {
        List list = [m]
        while (m.parentId)
            list << (m = Menu.get(m.parentId))
        return list
    }

    private Collection<MenuNode> generateSubTree(Menu parent, Collection<Menu> displayMenus) {
        def subList = new LinkedList<MenuNode>()
        displayMenus.findAll { it.parentId == parent.id }.sort().each {
            subList.add(new MenuNode(it, generateSubTree(it, displayMenus)))
        }
        return subList
    }
}


class MenuNode {
    MenuNode(Menu m, Collection<Menu> sub) {
        this.menu = m
        this.subMenus = sub
    }
    Menu menu
    Collection<Menu> subMenus
}