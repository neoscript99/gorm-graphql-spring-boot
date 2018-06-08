package neo.script.gorm.general.service

import neo.script.gorm.general.domain.Token
import neo.script.gorm.general.domain.User
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class TokenService extends AbstractService<Token> {

    Token createToken(User user) {
        saveEntity(new Token(user))
    }

    Map destoryToken(String id) {
        def token = get(id)
        if (token) {
            token.destroyed = true
            token.save()
            [success: true]
        } else
            [success: false, error: "无效token: $id"]
    }

    /**
     * 检查token是否过期、摧毁，如果token有效重置过期时间
     * @param id
     * @return
     */
    Map validateToken(String id) {
        def token = get(id)
        if (!token)
            [success: false, error: "无效token: $id"]
        else if (token.destroyed || token.expireTime.isBefore(LocalDateTime.now()))
            [success: false, error: "过期token: $id"]
        else {
            token.resetExpireTime()
            token.save()
            [success: true, user: token.user]
        }
    }
}
