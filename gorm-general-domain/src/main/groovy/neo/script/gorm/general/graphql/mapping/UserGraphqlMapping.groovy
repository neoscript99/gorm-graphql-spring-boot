package neo.script.gorm.general.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.general.domain.Token
import neo.script.gorm.general.domain.User
import neo.script.gorm.general.repositories.GeneralRepository
import neo.script.gorm.general.service.TokenService
import neo.script.gorm.general.service.UserService
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sun.util.resources.LocaleData

import java.time.LocalDateTime
import java.time.LocalTime

@Component
@GraphQLMappingFlag(User)
class UserGraphqlMapping extends GraphQLMapping {
    @Autowired
    UserService userService
    @Autowired
    TokenService tokenService

    UserGraphqlMapping() {
        exclude('password')
        query('login', 'LoginInfo') {
            description 'Login query, password is MD5 String'
            argument('username', String)
            argument('password', String)
            dataFetcher(new LoginDataFetcher())
            returns {
                field('success', Boolean)
                field('token', String)
                field('error', String)
            }
        }
        query('logout', 'LoginOutInfo') {
            description 'Logout'
            argument('token', String)

            dataFetcher(new LogoutDataFetcher())
            returns {
                field('success', Boolean)
                field('error', String)
            }
        }
    }

    class LoginDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            def result = userService.login(environment.getArgument('username'), environment.getArgument('password'));
            if (result.success)
                [success: true, token: tokenService.createToken().id]
            else
                result
        }
    }

    class LogoutDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return User.findByNameAndPassord(environment.getArgument('username'), environment.getArgument('password'))
        }
    }
}
