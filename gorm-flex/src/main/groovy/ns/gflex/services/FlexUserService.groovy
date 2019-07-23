package ns.gflex.services

import neo.script.gorm.general.domain.sys.*
import neo.script.gorm.general.service.UserService
import neo.script.util.EncoderUtil
import ns.gflex.services.base.GFlexService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class FlexUserService extends GFlexService {
    @Autowired
    UserService userService;

    def save(Map map) {
        if (map.password)
            map.password = EncoderUtil.sha256(map.password)
        else {
            User oldUser = User.get(map.id);
            oldUser.discard()
            map.password = oldUser.password
        }
        map.dept = Department.get(map.dept.id)

        super.save(map, true)
    }

    void addUserRole(def userId, def roleId) {
        if (!count([role: [idEq: [roleId]], user: [idEq: [userId]]], UserRole))
            saving(new UserRole(user: User.get(userId), role: Role.get(roleId)))
    }

    void deleteUserRoles(def userId, List roles) {
        log.info "delete User Roles ${userId}"
        if (roles) {
            UserRole.executeUpdate("delete UserRole where user.id = $userId and role.id in (${roles*.id.join(',')})")
        }
    }

    List getRoleUsers(def roleId, String account) {
        Map param = [user: [eq: [['editable', true]]], role: [idEq: [roleId]]]
        if (account)
            param.user.like = [['account', "%$account%"]]
        list(param, UserRole)*.user
    }

    List getUserRoles(def userId) {
        userService.getUserRoles(userId)
    }

    def login(String account, String password) {
        //mx.utils.SHA256生成的编码和guava有区别，暂时先用明文
        Map result = userService.login(account, EncoderUtil.sha256(password))
        if (result.success) {
            flexSession.setAttribute(GFlexService.SESSION_ACCOUNT_ID, result.user.account)
            return result.user
        } else
            return [hasMessage: true, message: result.error, isError: true]

    }

    void changePassword(String account, String oriPassword, String newPassword) {
        def user = User.findByAccountAndPassword(account, EncoderUtil.sha256(oriPassword))
        if (user)
            user.password = EncoderUtil.sha256(newPassword)
        else
            throw new RuntimeException('原密码错误，无法修改')
    }

    @Override
    public Class getDomainClass() {
        User.class;
    }
}
