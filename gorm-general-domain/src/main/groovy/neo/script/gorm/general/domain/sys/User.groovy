package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.data.initializer.initialize.InitializeDomian
import neo.script.util.EncoderUtil

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name,dept')
@EqualsAndHashCode(includes = 'account')
@InitializeDomian(depends = [Department])
class User {
    static final User ADMIN = (new User(account: 'admin', name: '系统管理员', dept: Department.HEAD_OFFICE,
            editable: false, password: EncoderUtil.sha256('admin')))
    static final User ANONYMOUS = (new User(account: 'anonymous', name: '匿名帐号', dept: Department.HEAD_OFFICE,
            editable: false, password: EncoderUtil.sha256('anonymous')))
    static final User TEST_USER = (new User(account: 'test.user', name: '测试用户', dept: Department.HEAD_OFFICE,
            editable: true, password: EncoderUtil.sha256('test')))

    String id
    String account
    String password = EncoderUtil.sha256('change-it')
    String name
    Boolean editable = true
    Boolean enabled = true

    static belongsTo = [dept: Department]

    static mapping = {
        dept fetch: 'join', lazy: false
    }
    static constraints = {
        account unique: true
        password maxSize: 64
    }

    static final initList = [ADMIN, ANONYMOUS, TEST_USER]
    static graphql
}
