package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.util.EncoderUtil

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name,dept')
@EqualsAndHashCode(includes = 'account')
@InitializeDomian(depends = [Department])
/**
 * 管理后台的系统用户信息
 * sys包下都是管理后台相关domain
 */
class User {
    String id
    String account
    String password = EncoderUtil.sha256('anonymous')
    String name
    Boolean editable = true
    Boolean enabled = true

    Department dept

    //User lastUser;
    Date lastUpdated;
    Date dateCreated;

    static mapping = {
        dept fetch: 'join', lazy: false
        //lastUser fetch: 'join', lazy: false
    }
    static constraints = {
        account unique: true
        password maxSize: 80
        //lastUser nullable: true
    }

    static graphql

    static final User ADMIN = (new User(account: 'admin', name: '系统管理员', dept: Department.HEAD_OFFICE,
            editable: false))
    static final User ANONYMOUS = (new User(account: 'anonymous', name: '匿名帐号', dept: Department.HEAD_OFFICE,
            editable: false))
    static final User TEST_USER = (new User(account: 'test.user', name: '测试用户', dept: Department.HEAD_OFFICE,
            editable: true))

    static final initList = [ADMIN, ANONYMOUS, TEST_USER]
}
