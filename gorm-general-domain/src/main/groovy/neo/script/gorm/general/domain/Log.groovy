package neo.script.gorm.general.domain

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@Entity
@ToString(includes = 'message')
@EqualsAndHashCode(includes = 'id')
class Log {
    String id
    String ownerId;
    String message;
    String ipAddress;
    String account
    Date dateCreated
    static mapping = {
        id generator: 'increment'
    }

    static constraints = {
        message maxSize: 1024
        ownerId nullable: true
        account nullable: true
    }
}