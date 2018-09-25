package ns.gflex.services

import neo.script.gorm.general.domain.sys.Department
import neo.script.gorm.general.domain.sys.Role
import neo.script.gorm.general.domain.sys.User
import neo.script.gorm.general.domain.sys.UserRole
import neo.script.util.EncoderUtil
import ns.gflex.services.base.GFlexService
import org.springframework.stereotype.Service;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
@Service
class FlexUserService extends GFlexService {

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

    List getRoleUsers(Long roleId, String account) {
        Map param = [user: [eq: [['editable', true]]], role: [idEq: [roleId]]]
        if (account)
            param.user.like = [['account', "%$account%"]]
        list(param, UserRole)*.user
    }

    List getUserRoles(Long userId) {
        list([user: [idEq: [userId]]], UserRole)*.role
    }

    def login(String account, String password) {
        def user = User.findByAccountAndPassword(account, EncoderUtil.md5(password))
        String msg;
        if (user) {
            if (!user.enabled)
                msg = '用户帐号失效，请联系管理员处理';
            flexSession.setAttribute(GFlexService.SESSION_ACCOUNT_ID, user.account)
            return user
        } else {
            if (User.findByAccount(account))
                msg = '密码错误'
            else
                msg = '用户名不存在'
        }

        if (msg) {
            log.warn("用户登录失败：$msg")
            [hasMessage: true, message: msg, isError: true]
        } else
            user
    }

    void changePassword(String account, String oriPassword, String newPassword) {
        def user = User.findByAccountAndPassword(account, EncoderUtil.md5(oriPassword))
        if (user)
            user.password = EncoderUtil.md5(newPassword)
        else
            throw new RuntimeException('原密码错误，无法修改')
    }

    @Override
    public Class getDomainClass() {
        User.class;
    }
}
