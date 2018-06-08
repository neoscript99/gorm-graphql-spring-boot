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
    static final Integer DEFAULT_EXPIRE_MINUTES = 1

    Token() {
        resetExpireTime()
    }

    Token(User user) {
        this.user = user
        resetExpireTime()
    }

    String id
    Date dateCreated
    Date lastUpdated
    Integer refreshTimes = 0
    LocalDateTime expireTime
    Boolean destroyed = false
    static belongsTo = [user: User];

    void resetExpireTime() {
        expireTime = LocalDateTime.now().plusMinutes(DEFAULT_EXPIRE_MINUTES)
        refreshTimes++
    }
    static mapping = {
        id generator: 'uuid2'
        user fetch: 'join', lazy: false
    }
}
