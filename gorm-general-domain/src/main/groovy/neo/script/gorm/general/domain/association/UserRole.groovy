package neo.script.gorm.general.domain.association

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.data.initializer.initialize.InitializeDomian
import neo.script.gorm.general.domain.User
import neo.script.gorm.general.domain.Role

@Entity
@InitializeDomian(depends = [User, Role])
@ToString(includes = ['role', 'user'])
@EqualsAndHashCode(includes = ['role', 'user'])
class UserRole implements Serializable {

    String id
    Role role
    User user

    static mapping = {
        role fetch: 'join', lazy: false
        user fetch: 'join', lazy: false
    }

    static constraints = {
        role(unique: 'user')
    }
    static initList = [
            (new UserRole(user: User.ADMIN, role: Role.ADMINISTRATORS)),
            (new UserRole(user: User.ANONYMOUS, role: Role.PUBLIC))]
}
