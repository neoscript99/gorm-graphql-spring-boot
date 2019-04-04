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
    MenuNode getUserTree(User user) {
        return generateTree(getMenuByRoles(UserRole.findAllByUser(user)*.role))
    }

    MenuNode getRoleTree(Role role) {
        return generateTree(getMenuByRoles([role]))
    }

    MenuNode getFullTree() {
        return generateTree(list())
    }

    protected Collection<Menu> getMenuByRoles(List<Role> roleList) {
        def roleMenus = new HashSet<Menu>()
        roleList.each { role ->
            roleMenus += RoleMenu.findAllByRole(role)*.menu
        }
        return roleMenus
    }

    protected MenuNode generateTree(Collection<Menu> menuList) {
        def displayMenus = new HashSet<Menu>()
        menuList.each {
            displayMenus += getGenealogy(it)
        }
        //root节点为单一的，无parent的节点
        def root = displayMenus.find { !it.parentId }
        return new MenuNode(root, generateSubTree(root, displayMenus))
    }

    private List getGenealogy(Menu m) {
        List list = [m]
        while (m.parentId)
            list << (m = get(m.parentId))
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

    @Override
    public String toString() {
        return """\n$menu - [$subMenus]""";
    }
}
