package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
class LivebosServer {
    String id
    String serverName
    String serverRoot
    String restUser
    String restPassword
    Boolean enabled = true
    String restPath = '/service/LBREST'
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
        serverRoot maxSize: 128
        loginUri maxSize: 128
        userInfoUri maxSize: 128
        noticeUri maxSize: 128
        sessionId nullable: true, maxSize: 128
    }
    static graphql = true
}
