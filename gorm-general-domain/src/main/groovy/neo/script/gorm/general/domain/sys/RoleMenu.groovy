package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includes = ['role', 'menu'])
@EqualsAndHashCode(includes = ['role', 'menu'])
class RoleMenu implements Serializable {

    String id
    Role role
    Menu menu

    static mapping = {
        role fetch: 'join', lazy: false
        menu fetch: 'join', lazy: false
    }

    static constraints = {
        menu(unique: 'role')
    }
}
