package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Token
import neo.script.gorm.general.domain.sys.User
import net.unicon.cas.client.configuration.CasClientConfigurationProperties
import org.jasig.cas.client.util.AssertionHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CasClientService {
    @Value('${gorm.cas.defaultRoles}')
    String casDefaultRoles
    @Value('${gorm.cas.client.enabled}')
    Boolean clientEnabled
    @Autowired(required = false)
    CasClientConfigurationProperties configProps;
    @Autowired
    UserService userService
    @Autowired
    TokenService tokenService

    Token createTokenByCas() {
        if (casAccount) {
            def user = getUserByCas()
            def roles = user ? userService.getUserRoleCodes(user) : casDefaultRoles
            return tokenService.createToken(casAccount, roles)
        }
    }

    String getCasAccount() {
        if (AssertionHolder.assertion)
            return AssertionHolder.assertion.principal.name
    }

    User getUserByCas() {
        def account = this.casAccount;
        return account ? userService.findByAccount(account) : null
    }

    String getLogoutUrl() {
        if (configProps)
            return "$configProps.serverUrlPrefix/logout?service=$configProps.clientHostUrl/index.html"
    }
}
