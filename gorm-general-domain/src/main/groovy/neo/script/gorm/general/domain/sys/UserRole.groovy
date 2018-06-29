package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian

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
