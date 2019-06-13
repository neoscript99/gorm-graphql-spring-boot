package neo.script.gorm.portal.service

import neo.script.gorm.portal.domain.ptl.live.LiveQuery
import neo.script.gorm.portal.domain.ptl.live.LiveServer
import neo.script.util.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class LiveServerServiceSpec extends Specification {
    static private Logger log = LoggerFactory.getLogger(LiveServerServiceSpec.class)

    def restTemplateTest() {
        given:

        StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        def it = LiveServer.DEMO_SERVER

        //不能直接用restTemplate.getForObject(it.restRoot + it.loginUri, LoginRes.class, it.restUser, it.restPassword)
        //该服务不接受Accept header [application/json, application/*+json]
        def result = restTemplate.getForObject(it.restRoot + it.loginUri, String.class, it.restUser, it.restPassword)
        LiveServerService.LoginRes loginRes = JsonUtil.fromJson(result, LiveServerService.LoginRes.class);
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
        def liveServer = LiveServer.DEMO_SERVER;
        def lss = new LiveServerService()
        def loginRes = lss.userLogin(LiveServer.DEMO_SERVER)
        liveServer.sessionId = loginRes.sessionId

        log.info(lss.getUserInfo(liveServer, 'admin'))
        log.info(lss.queryNotices(liveServer, 'admin', '0'))
        log.info(lss.objectQuery(LiveQuery.DEMO_QUERY))
        expect:
        true
    }
}
