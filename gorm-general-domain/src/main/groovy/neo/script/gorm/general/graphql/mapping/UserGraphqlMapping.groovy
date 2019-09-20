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
    @Autowired
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
                field('account', String)
                field('error', String)
            }
        }
        mutation('sessionLogin', 'SessionLoginInfo') {
            description '检查Session，确认当前客户端是否已登录.'
            dataFetcher(new SessionLoginDataFetcher())
            returns {
                field('success', Boolean)
                field('token', String)
                field('user', User)
                field('roles', String)
                field('account', String)
                field('error', String)
            }
        }
        query('getCasConfig', 'CasConfigInfo') {
            description 'Cas client config.'
            dataFetcher(new CasConfigDataFetcher())
            returns {
                field('clientEnabled', Boolean)
                field('casServerRoot', String)
                field('defaultRoles', String)
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

    class SessionLoginDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            //检查session中是否包含token
            def token = gormSessionBean.token
            if (token) {
                def user = userService.findByAccount(token.username)
                [success: true,
                 user   : user,
                 roles  : token.roles,
                 account: token.username,
                 token  : token.id]
            } else
                [success: false,
                 error  : '服务端没有session信息']
        }
    }

    class CasConfigDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return [clientEnabled: casClientService.clientEnabled,
                    casServerRoot: casClientService.configProps.serverUrlPrefix,
                    defaultRoles : casClientService.casDefaultRoles]
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
                 account: result.user.account,
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
