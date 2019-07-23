package neo.script.gorm.general.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.service.MenuNode
import neo.script.gorm.general.service.MenuService
import neo.script.gorm.general.service.RoleService
import neo.script.gorm.general.service.TokenService
import neo.script.gorm.general.service.UserService
import neo.script.gorm.general.util.TokenHolder
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(Menu)
class MenuGraphqlMapping extends GraphQLMapping {
    @Autowired
    MenuService menuService
    @Autowired
    TokenService tokenService
    @Autowired
    UserService userService
    @Autowired
    RoleService roleService

    MenuGraphqlMapping() {
        query('menuTree', MenuNode) {
            description 'get sys user menu tree'
            dataFetcher(new MenuTreeDataFetcher())
        }
    }

    class MenuTreeDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            def token = TokenHolder.getToken()
            def account = token.username;
            def user = userService.findByAccount(account);
            if (user)
                return menuService.getUserTree(user)
            else {
                def roleList = roleService.findByCodes(token.roles.split(','))
                return menuService.getRolesTree(roleList)
            }
        }
    }
}
