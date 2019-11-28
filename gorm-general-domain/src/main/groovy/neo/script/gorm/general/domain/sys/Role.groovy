package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import org.apache.xmlbeans.impl.xb.xsdschema.Public

/**
 * 创建时间 Dec 14, 2010
 * @author wangchu
 */
@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'roleName')
@EqualsAndHashCode(includes = 'roleCode')
@InitializeDomian
class Role {
    //管理员，所有菜单权限
    static final Role ADMINISTRATORS = new Role(roleName: '系统管理员', roleCode: 'Administrators',
            editable: false, description: 'System Administrators.')

    static final Role NORMAL_USERS = new Role(roleName: '普通用户', roleCode: 'NormalUsers',
            editable: false, description: 'Normal users has user related menus.')

    //公开用户，可支持匿名用户查看网站通知等开放功能
    static final Role PUBLIC = new Role(roleName: '默认角色', roleCode: ' Public',
            editable: false, description: 'Public role associate with public menus.')

    String id
    String roleName
    String roleCode
    String description
    Boolean editable = true
    Boolean enabled = true
    Date dateCreated
    Date lastUpdated
    static mapping = {
    }
    static constraints = {
        description nullable: true, blank: true, maxSize: 128
        roleCode unique: true
    }

    static initList = [ADMINISTRATORS, NORMAL_USERS, PUBLIC]
    static graphql = true
}
