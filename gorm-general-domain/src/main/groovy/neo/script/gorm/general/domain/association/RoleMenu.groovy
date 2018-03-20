package neo.script.gorm.general.domain.association

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.domain.Menu
import neo.script.gorm.general.domain.Role

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
