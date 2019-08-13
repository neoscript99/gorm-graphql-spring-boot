package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Token
import neo.script.gorm.general.domain.sys.User
import net.unicon.cas.client.configuration.CasClientConfigurationProperties
import org.jasig.cas.client.util.AssertionHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(value = "gorm.cas.client.enabled", havingValue = "true", matchIfMissing = false)
class CasClientService {
    @Value('${gorm.cas.defaultRoles}')
    String casDefaultRoles
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
        return userService.findByAccount(account)
    }
}
