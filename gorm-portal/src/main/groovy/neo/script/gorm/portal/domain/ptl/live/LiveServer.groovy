package neo.script.gorm.portal.domain.ptl.live

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.ptl.Portal

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = Portal)
class LiveServer {
    String id

    String restRoot
    String restUser
    String restPassword
    Portal portal
    String loginPath = '/userService/login'
    String userInfoPath = '/userService/getUserInfo'
    String noticePath = '/messageCenter/queryNotices'
    String objectQueryPath = '/lbObject/query'
    //定时更新
    String sessionId

    Date dateCreated
    Date lastUpdated

    static mapping = {
        portal fetch: 'join', lazy: false
    }
    static constraints = {
        restRoot maxSize: 128
        sessionId nullable: true
    }
    static graphql = true
    static LiveServer LIVEBPM_DEMO_SERVER = new LiveServer('http://114.115.153.164:7070/service/LBREST', 'rest', '000000', Portal.LIVEBPM_PORTAL_DEMO)
    static initList = [LIVEBPM_DEMO_SERVER]
}
