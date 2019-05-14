package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian

import java.time.LocalDateTime

@Entity
@ToString(includePackage = false, includes = 'expireTime,lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class Token {

    String id
    //如果一直未访问的到期时间
    LocalDateTime expireTime
    Boolean destroyed = false
    //到期之前如有访问，可刷新次数
    Integer maxRefreshTimes
    Integer refreshTimes = 0
    String username
    //用户类型，sys - 后台管理用户，pub - 公网用户
    String userType = 'sys'
    String role

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator: 'assigned'
    }
    static constraints = {
        username nullable: true
        role nullable: true
    }

    static initList = [new Token([
            id             : 'gorm-dev-token',
            expireTime     : LocalDateTime.of(2021, 1, 1, 1, 1),
            maxRefreshTimes: 10,
            username           : 'admin'])]
}
