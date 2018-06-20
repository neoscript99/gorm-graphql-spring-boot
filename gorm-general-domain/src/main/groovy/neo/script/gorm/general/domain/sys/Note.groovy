package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 系统通知
 * @author neo
 * 2012-6-11
 */
@Entity
@ToString(includes = 'title')
@EqualsAndHashCode(includes = 'id')
class Note {
    String id
    String title;
    String content;
    Integer attachNum;
    User lastUser;
    Date lastUpdated;
    Date dateCreated;
    static mapping = {
        lastUser lazy: false, fetch: 'join'
    }
    static constraints = {
        title maxSize: 256
        content nullable: true, maxSize: 1024
    }
    static graphql = true
}
