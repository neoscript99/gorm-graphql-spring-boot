package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import java.time.LocalDateTime

@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'expireTime,lastUpdated')
@EqualsAndHashCode(includes = 'id')
class Token {

    String id
    LocalDateTime expireTime
    Boolean destroyed = false

    Date dateCreated
    Date lastUpdated
}
