package neo.script.gorm.portal.service

import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
import neo.script.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class LivebosServerServiceSpec extends Specification {
    static private Logger log = LoggerFactory.getLogger(LivebosServerServiceSpec.class)

    def restTemplateTest() {
        given:

        StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        def it = LivebosServer.DEMO_SERVER

        //不能直接用restTemplate.getForObject(url, LoginRes.class, it.restUser, it.restPassword)
        //该服务不接受Accept header [application/json, application/*+json]
        def result = restTemplate.getForObject(it.serverRoot + it.restPath + it.loginUri, String.class, it.restUser, it.restPassword)
        LivebosServerService.LoginRes loginRes = JsonUtil.fromJson(result, LivebosServerService.LoginRes.class);
        if (loginRes.result == 1) {
            log.info('Livebos登入成功：{}', loginRes)
            it.sessionId = loginRes.sessionId
        } else
            log.error('Livebos登入失败：{}', loginRes)

        expect:
        loginRes.result == 1
    }

    def 'livebos service test'() {
        given:
        def livebosServer = LivebosServer.DEMO_SERVER;
        def lss = new LivebosServerService()
        def loginRes = lss.userLogin(LivebosServer.DEMO_SERVER)
        livebosServer.sessionId = loginRes.sessionId

        log.info(lss.getUserInfo(livebosServer, 'admin'))
        log.info(lss.queryNotices(livebosServer, 'admin', '0'))
        log.info(lss.objectQuery(LivebosQuery.USER_LINK))
        expect:
        true
    }

    def 'livebos objectQuery test'() {
        given:
        def livebosServer = new LivebosServer('路桥LivebosServer',
                'http://114.115.153.164:7070',
                'rest', '000000')
        def lss = new LivebosServerService()
        def loginRes = lss.userLogin(LivebosServer.DEMO_SERVER)
        livebosServer.sessionId = loginRes.sessionId

        def livebosQuery = new LivebosQuery('公司要闻', 'LivebosQuery', livebosServer,
                'T_SYS_NewsInfo', "FClass=2", 'DISPLAY')
        log.info(lss.objectQuery(livebosQuery))
        expect:
        true
    }

    def 'session invalid test'() {
        given:
        def livebosServer = LivebosServer.DEMO_SERVER;
        def lss = new LivebosServerService()
        log.info(lss.getUserInfo(livebosServer, 'admin'))
        log.info(lss.queryNotices(livebosServer, 'admin', '0'))
        log.info(lss.objectQuery(LivebosQuery.USER_LINK))
        expect:
        true
    }
}
