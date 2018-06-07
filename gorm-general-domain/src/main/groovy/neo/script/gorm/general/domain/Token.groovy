package neo.script.gorm.general.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.time.LocalDateTime

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'user,lastUpdated')
@EqualsAndHashCode(includes = 'id')
class Token {
    User user;
    Date dateCreated
    Date lastUpdated
    Integer refreshTimes = 0
    LocalDateTime expireTime

    static mapping = {
        id generator: 'uuid2'
    }
}
