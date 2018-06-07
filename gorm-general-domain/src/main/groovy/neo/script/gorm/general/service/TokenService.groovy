package neo.script.gorm.general.service

import neo.script.gorm.general.domain.Token
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class TokenService extends AbstractService<Token> {

    Token createToken() {
        def token = saveEntity(new Token(expireTime: LocalDateTime.now().plusMinutes(3)))
    }

    void destoryToken(def id){
        get(id)
    }
}
