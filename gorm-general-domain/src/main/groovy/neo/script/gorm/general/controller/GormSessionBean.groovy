package neo.script.gorm.general.controller

import neo.script.gorm.general.domain.sys.Token
import neo.script.gorm.general.service.CasClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
class GormSessionBean {
    @Autowired(required = false)
    CasClientService casClientService

    private Token token

    Token getToken() {
        if (!token && casClientService)
            token = casClientService.createTokenByCas()
        return token
    }

    void setToken(Token token) {
        this.token = token
    }
}
