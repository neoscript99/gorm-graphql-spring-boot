package neo.script.gorm.general.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.general.controller.GormSessionBean
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.service.CasClientService
import neo.script.gorm.general.service.TokenService
import neo.script.gorm.general.service.UserService
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(User)
class UserGraphqlMapping extends GraphQLMapping {
    @Autowired
    UserService userService
    @Autowired(required = false)
    CasClientService casClientService
    @Autowired
    TokenService tokenService
    @Autowired(required = false)
    GormSessionBean gormSessionBean

    UserGraphqlMapping() {
        exclude('password')
        mutation('login', 'LoginInfo') {
            description 'Login query, password is sha256 String'
            argument('username', String)
            argument('password', String)
            dataFetcher(new LoginDataFetcher())
            returns {
                field('success', Boolean)
                field('token', String)
                field('user', User)
                field('roles', String)
                field('error', String)
            }
        }
        mutation('casLogin', 'CasLoginInfo') {
            description 'Login system use cas filter.'
            dataFetcher(new CasLoginDataFetcher())
            returns {
                field('success', Boolean)
                field('token', String)
                field('user', User)
                field('roles', String)
                field('casAccount', String)
                field('casServerRoot', String)
                field('error', String)
            }
        }
        mutation('logout', 'LoginOutInfo') {
            description 'Logout'
            argument('token', String)

            dataFetcher(new LogoutDataFetcher())
            returns {
                field('success', Boolean)
                field('error', String)
            }
        }
    }

    class CasLoginDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            //通过cas登录的用户，如果存在匹配的User，返回这个User相关角色，否则返回默认角色
            def account = casClientService.casAccount
            if (account) {
                def user = casClientService.getUserByCas()
                def roles = user ? userService.getUserRoleCodes(user) : casClientService.casDefaultRoles
                def token = casClientService.createTokenByCas()
                if (gormSessionBean)
                    gormSessionBean.token = token
                [success      : true,
                 user         : user,
                 roles        : roles,
                 casAccount   : account,
                 casServerRoot: casClientService.configProps.serverUrlPrefix,
                 token        : token.id]
            } else
                [success: false,
                 error  : '未登录CAS']
        }
    }

    class LoginDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            def result = userService.login(environment.getArgument('username'), environment.getArgument('password'));

            if (result.success) {
                def roles = userService.getUserRoleCodes(result.user)
                def token = tokenService.createToken(result.user.account, roles)
                if (gormSessionBean)
                    gormSessionBean.token = token
                [success: true,
                 user   : result.user,
                 roles  : roles,
                 token  : token.id]
            } else
                result
        }
    }

    class LogoutDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            tokenService.destoryToken(environment.getArgument('token'))
        }
    }
}
