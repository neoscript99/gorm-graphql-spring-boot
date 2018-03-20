package neo.script.gorm.general.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.data.initializer.initialize.InitializeDomian

/**
 * 系统参数
 * @since Dec 23, 2010
 * @author wangchu
 */
@Entity
@InitializeDomian(value = 'paramList', depends = [ParamType, User])
@ToString(includePackage = false, includes = 'name,code')
@EqualsAndHashCode(includes = 'code')
class Param {
    static final Param ALLOW_ANONYMOUS_LOGIN = (new Param(code: 'AllowAnonymousLogin', name: '允许匿名登录',
            value: 'false', type: ParamType.SYSTEM, validExp: '^(true|false)$', validDescribe: 'true或者false', lastUser: User.ADMIN))
    static final Param SPARE_MINTUES = (new Param(code: 'SpareMintues', name: '空闲多少分钟后自动退出（不能大于web容器session过期时间）',
            value: '20', type: ParamType.SYSTEM, validExp: '^\\d+$', validDescribe: '整数', lastUser: User.ADMIN))
    static final Param HOLD_HEART_BEAT = (new Param(code: 'HoldHeartBeat', name: '保持登录状态',
            value: 'false', type: ParamType.SYSTEM, validExp: '^(true|false)$', validDescribe: 'true或者false', lastUser: User.ADMIN))

    String id
    String code
    String name
    String value
    ParamType type
    String validExp //属性值类型正则表达式校验
    String validDescribe //校验说明
    User lastUser
    Date lastUpdated

    static mapping = {
        type lazy: false, fetch: 'join'
        lastUser lazy: false, fetch: 'join'
    }

    static constraints = {
        code unique: true
        lastUser nullable: true
        value maxSize: 200
        name maxSize: 200
        validDescribe maxSize: 200
    }
    static final paramList = [ALLOW_ANONYMOUS_LOGIN, SPARE_MINTUES, HOLD_HEART_BEAT]
    static graphql = true
}
