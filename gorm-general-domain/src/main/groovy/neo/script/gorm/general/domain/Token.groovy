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

    String id
    Date dateCreated
    Date lastUpdated
    LocalDateTime expireTime
    Boolean destroyed = false
    static belongsTo = [user: User];

    static mapping = {
        id generator: 'uuid2'
        user fetch: 'join', lazy: false
    }
}
