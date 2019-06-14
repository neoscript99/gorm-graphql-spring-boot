package neo.script.gorm.portal.service

import groovy.transform.ToString
import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.ptl.live.LiveQuery
import neo.script.gorm.portal.domain.ptl.live.LiveServer
import neo.script.util.JsonUtil
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

import java.nio.charset.StandardCharsets

@Service
class LiveServerService extends AbstractService<LiveServer> {
    StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    RestTemplate restTemplate = new RestTemplateBuilder()
            .additionalMessageConverters(m)
            .additionalMessageConverters(new AllEncompassingFormHttpMessageConverter())
            .build();

    @Scheduled(fixedDelay = 60000000L, initialDelay = 1000L)
    public void refershSessionId() {
        listEnabled().each {
            def loginRes = userLogin(it)
            if (loginRes.result == 1) {
                log.info('Livebos登入成功：{}', loginRes)
                it.sessionId = loginRes.sessionId
            } else
                log.error('Livebos登入失败：{}', loginRes)
        }
    }

    def userLogin(LiveServer liveServer) {
        def url = liveServer.serverRoot + liveServer.restPath + liveServer.loginUri
        //不能直接用restTemplate.getForObject(url, LoginRes.class, it.restUser, it.restPassword)
        //该服务不接受Accept header [application/json, application/*+json]
        def result = restTemplate.getForObject(url, String.class, liveServer.restUser, liveServer.restPassword)
        return JsonUtil.fromJson(result, LoginRes.class);
    }

    def getUserInfo(String liveServerId, String userId) {
        getUserInfo(get(liveServerId), userId)
    }

    def getUserInfo(LiveServer liveServer, String userId) {
        def url = liveServer.serverRoot + liveServer.restPath + liveServer.userInfoUri
        return restTemplate.getForObject(url, String.class, userId, liveServer.sessionId)
    }


    def queryNotices(String liveServerId, String userId, String type) {
        queryNotices(get(liveServerId), userId, type)
    }

    def queryNotices(LiveServer liveServer, String userId, String type) {
        def url = liveServer.serverRoot + liveServer.restPath + liveServer.noticeUri
        return restTemplate.getForObject(url, String.class, userId, liveServer.sessionId, type)
    }

    def objectQuery(String liveQueryId) {
        objectQuery(generalRepository.get(LiveQuery, liveQueryId))
    }

    def objectQuery(LiveQuery liveQuery) {
        def liveServer = liveQuery.liveServer
        def url = liveServer.serverRoot + liveServer.restPath + liveServer.objectQueryUri
        def requestData = [objectName : liveQuery.objectName,
                           condition  : liveQuery.condition,
                           queryOption: [
                                   valueOption: liveQuery.valueOption,
                                   batchNo    : liveQuery.batchNo,
                                   batchSize  : liveQuery.batchSize,
                                   queryCount : liveQuery.queryCount
                           ]]

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add('sessionId', liveServer.sessionId)
        parts.add('requestData', requestData)

        return restTemplate.postForObject(url, parts, String.class)
    }

    @ToString(includePackage = false)
    static class LoginRes {
        String sessionId
        Integer result //1为成功
        String message
    }

}
