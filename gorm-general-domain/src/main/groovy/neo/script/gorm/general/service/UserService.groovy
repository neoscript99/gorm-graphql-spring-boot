package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Department
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.domain.sys.UserRole
import neo.script.util.EncoderUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class UserService extends AbstractService<User> {

    @Transactional(readOnly = true)
    Map login(String account, String password) {
        def user = findFirst([eq: [['account', account]]])
        String msg;
        if (user) {
            if (!user.enabled)
                msg = '用户帐号失效';
            if (user.password != password)
                msg = '密码错误'
        } else
            msg = '用户名不存在'

        if (msg) {
            log.warn("用户登录失败：$msg")
            [success: false, error: msg]
        } else
            [success: true, user: user]
    }
}
