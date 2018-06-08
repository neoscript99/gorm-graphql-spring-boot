package neo.script.gorm.general.service

import neo.script.gorm.general.domain.Department
import neo.script.gorm.general.domain.Role
import neo.script.gorm.general.domain.User
import neo.script.gorm.general.domain.association.UserRole
import neo.script.util.EncoderUtil
import org.springframework.stereotype.Service
import grails.gorm.transactions.Transactional

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class UserService extends AbstractService<User> {

    def save(Map map) {
        if (map.password)
            map.password = EncoderUtil.md5(map.password)
        else {
            User oldUser = User.get(map.id);
            oldUser.discard()
            map.password = oldUser.password
        }
        map.dept = Department.get(map.dept.id)

        super.save(map, true)
    }

    void addUserRole(Long userId, Long roleId) {
        if (!count([role: [idEq: [roleId]], user: [idEq: [userId]]], UserRole))
            saving(new UserRole(user: User.get(userId), role: Role.get(roleId)))
    }

    void deleteUserRoles(Long userId, List roles) {
        log.info "delete User Roles ${userId}"
        if (roles) {
            UserRole.executeUpdate("delete UserRole where user.id = $userId and role.id in (${roles*.id.join(',')})")
        }
    }

    @Transactional(readOnly = true)
    List getRoleUsers(Long roleId, String account) {
        Map param = [user: [eq: [['editable', true]]], role: [idEq: [roleId]]]
        if (account)
            param.user.like = [['account', "%$account%"]]
        list(param, UserRole)*.user
    }

    @Transactional(readOnly = true)
    List getUserRoles(Long userId) {
        list([user: [idEq: [userId]]], UserRole)*.role
    }

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

    void changePassword(String account, String oriPassword, String newPassword) {
        def user = User.findByAccountAndPassword(account, oriPassword)
        if (user)
            user.password = newPassword
        else
            throw new RuntimeException('原密码错误，无法修改')
    }
}
