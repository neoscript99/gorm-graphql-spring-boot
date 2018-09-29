package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString


@Entity
@ToString(includes = 'message')
@EqualsAndHashCode(includes = 'id')
class Log {
    String id
    String ownerId;
    String ownerName
    String message;
    String ipAddress;
    String account
    Date dateCreated

    static constraints = {
        message maxSize: 1024
        ownerId nullable: true, maxSize: 80
        account nullable: true
    }
}