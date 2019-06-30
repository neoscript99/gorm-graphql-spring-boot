package neo.script.gorm.general.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.service.TokenService
import neo.script.gorm.general.service.UserService
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.jasig.cas.client.util.AssertionHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(User)
class UserGraphqlMapping extends GraphQLMapping {
    @Autowired
    UserService userService
    @Autowired
    TokenService tokenService

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
                field('error', String)
            }
        }
        mutation('casLogin', 'CasLoginInfo') {
            description 'Login system use cas filter.'
            dataFetcher(new CasLoginDataFetcher())
            returns {
                field('success', Boolean)
                field('token', String)
                field('user', String)
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
            if (AssertionHolder.assertion)
                [success: true,
                 user   : AssertionHolder.assertion.principal.name,
                 token  : tokenService.createToken(AssertionHolder.assertion.principal.name, 'cas').id]
            else
                [success: false,
                 error  : '未登录CAS']
        }
    }

    class LoginDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            def result = userService.login(environment.getArgument('username'), environment.getArgument('password'));
            if (result.success)
                [success: true,
                 user   : result.user,
                 token  : tokenService.createToken(result.user.account, 'admin').id]
            else
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
