package neo.script.gorm.portal.domain.ptl.live

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class LiveServer {
    String id
    String serverName
    String restRoot
    String restUser
    String restPassword
    Boolean enabled = true
    String loginUri = '/userService/login?userId={userId}&password={password}'
    String userInfoUri = '/userService/getUserInfo?userId={userId}&sessionId={sessionId}'
    String noticeUri = '/messageCenter/queryNotices?userId={userId}&sessionId={sessionId}&type={type}'
    String objectQueryUri = '/lbObject/query'
    //定时更新
    String sessionId

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
        restRoot maxSize: 128
        loginUri maxSize: 128
        userInfoUri maxSize: 128
        noticeUri maxSize: 128
        sessionId nullable: true, maxSize: 128
    }
    static graphql = true
    static LiveServer DEMO_SERVER =
            new LiveServer('部门Livebpm环境',
                    'http://114.115.153.164:7070/service/LBREST',
                    'rest', '000000')
    static initList = [DEMO_SERVER]
}
