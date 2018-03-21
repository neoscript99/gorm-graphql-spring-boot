package neo.script.gorm.general.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.data.initializer.initialize.InitializeDomian
import neo.script.util.EncoderUtil
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name,dept')
@EqualsAndHashCode(includes = 'account')
@InitializeDomian(depends = [Department])
class User {
    static final User ADMIN = (new User(account: 'admin', name: '系统管理员', dept: Department.HEAD_OFFICE,
            editable: false, password: EncoderUtil.md5('admin')))
    static final User ANONYMOUS = (new User(account: 'anonymous', name: '匿名帐号', dept: Department.HEAD_OFFICE,
            editable: false, password: EncoderUtil.md5('anonymous')))

    String id
    String account
    String password = EncoderUtil.md5('change-it')
    String name
    Boolean editable = true
    Boolean enabled = true

    static belongsTo = [dept: Department]
    
    static mapping = {
        dept fetch: 'join', lazy: false
    }
    static constraints = { account unique: true }

    static final initList = [ADMIN, ANONYMOUS]
    static graphql = GraphQLMapping.build {
        exclude('password')
    }
}
