package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@Entity
@ToString(includePackage = false, includes = 'expireTime,lastUpdated')
@EqualsAndHashCode(includes = 'id')
class Token {

    String id
    //如果一直未访问的到期时间
    LocalDateTime expireTime
    Boolean destroyed = false
    //到期之前如有访问，可刷新次数
    Integer maxRefreshTimes
    Integer refreshTimes = 0
    String user
    String role

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator: 'assigned'
    }
    static constraints = {
        user nullable: true
        role nullable: true
    }
}
