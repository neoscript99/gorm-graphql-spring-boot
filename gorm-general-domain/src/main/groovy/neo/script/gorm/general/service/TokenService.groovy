package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Token
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.time.Duration
import java.time.LocalDateTime

@Service
class TokenService extends AbstractService<Token> {

    @Value('${token.expire.minutes}')
    Integer expireMinutes
    @Value('${token.expire.maxRefreshTimes}')
    Integer maxRefreshTimes

    Token createToken(String username, String roles) {
        return createToken(UUID.randomUUID().toString(), username, roles)
    }

    Token createToken(String id, String username, String roles) {
        return saveEntity(resetExpireTime(new Token([
                id             : id,
                maxRefreshTimes: maxRefreshTimes ?: 10,
                username       : username,
                roles          : roles])))
    }

    Map destoryToken(String id) {
        def token = get(id)
        if (token) {
            token.destroyed = true
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
            resetExpireTime(token)
            [success: true, token: token]
        }
    }


    private Token resetExpireTime(Token token) {
        def now = LocalDateTime.now()
        if (token.refreshTimes < token.maxRefreshTimes)
        //一定间隔后再刷新token，避免HibernateOptimisticLockingFailureException
            if (!token.expireTime || Duration.between(now, token.expireTime).toMinutes() < expireMinutes * 2 / 4) {
                token.expireTime = now.plusMinutes(expireMinutes)
                token.refreshTimes++
                /**
                 * 如果是类内部赋值expireTime
                 * 不会触发org.grails.datastore.mapping.dirty.checking.DirtyCheckable.markDirty
                 * markDirty后事务提交时才会自动保存
                 * 这里没有问题，原先在Token内部调用时有问题
                 */
                token.markDirty()
            }
        return token
    }
}
