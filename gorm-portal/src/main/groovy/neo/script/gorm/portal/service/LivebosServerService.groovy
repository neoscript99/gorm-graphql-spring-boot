package neo.script.gorm.portal.service

import groovy.transform.ToString
import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
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
class LivebosServerService extends AbstractService<LivebosServer> {
    StringHttpMessageConverter m = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    RestTemplate restTemplate = new RestTemplateBuilder()
            .additionalMessageConverters(m)
            .additionalMessageConverters(new AllEncompassingFormHttpMessageConverter())
            .build();

    @Scheduled(fixedDelay = 600000L, initialDelay = 5000L)
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

    def userLogin(LivebosServer livebosServer) {
        def url = livebosServer.serverRoot + livebosServer.restPath + livebosServer.loginUri
        //不能直接用restTemplate.getForObject(url, LoginRes.class, it.restUser, it.restPassword)
        //该服务不接受Accept header [application/json, application/*+json]
        def result = restTemplate.getForObject(url, String.class, livebosServer.restUser, livebosServer.restPassword)
        return JsonUtil.fromJson(result, LoginRes.class);
    }

    def getUserInfo(String livebosServerId, String userId) {
        getUserInfo(get(livebosServerId), userId)
    }

    def getUserInfo(LivebosServer livebosServer, String userId) {
        def url = livebosServer.serverRoot + livebosServer.restPath + livebosServer.userInfoUri
        checkResult(restTemplate.getForObject(url, String.class, userId, livebosServer.sessionId), UserInfoRes)
    }

    def queryNotices(String livebosServerId, String userId, String type) {
        queryNotices(get(livebosServerId), userId, type)
    }

    def queryNotices(LivebosServer livebosServer, String userId, String type) {
        def url = livebosServer.serverRoot + livebosServer.restPath + livebosServer.noticeUri
        checkResult(restTemplate.getForObject(url, String.class, userId, livebosServer.sessionId, type), NoticeRes)
    }

    def objectQuery(String livebosQueryId) {
        objectQuery(generalRepository.get(LivebosQuery, livebosQueryId))
    }

    def objectQuery(LivebosQuery livebosQuery) {
        def livebosServer = livebosQuery.livebosServer
        def url = livebosServer.serverRoot + livebosServer.restPath + livebosServer.objectQueryUri
        def requestData = [objectName : livebosQuery.objectName,
                           condition  : livebosQuery.condition,
                           queryOption: [
                                   valueOption: livebosQuery.valueOption,
                                   batchNo    : livebosQuery.batchNo,
                                   batchSize  : livebosQuery.batchSize,
                                   queryCount : livebosQuery.queryCount
                           ]]

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add('sessionId', livebosServer.sessionId)
        parts.add('requestData', requestData)

        checkResult(restTemplate.postForObject(url, parts, String.class), ObjectQueryRes)
    }

    String checkResult(String resString, Class<? extends LivebosRes> resClass) {
        LivebosRes res = JsonUtil.fromJson(resString, resClass)
        if (res.isSessionInvalid())
            refershSessionId()
        if (!res.isSuccess())
            log.error(res.toString())
        return resString
    }

    static interface LivebosRes {
        boolean isSuccess()

        boolean isSessionInvalid()
    }

    @ToString(includePackage = false)
    static class LoginRes implements LivebosRes {
        String sessionId
        Integer result //1为成功
        String message

        boolean isSuccess() {
            result == 1
        }

        boolean isSessionInvalid() {
            return false
        }
    }

    @ToString(includePackage = false)
    static class UserInfoRes implements LivebosRes {
        Integer status

        boolean isSuccess() {
            status == 1
        }

        boolean isSessionInvalid() {
            status == 0
        }
    }

    @ToString(includePackage = false)
    static class NoticeRes implements LivebosRes {
        Integer workflowCount
        Integer result
        String message

        boolean isSuccess() {
            result == 1
        }

        boolean isSessionInvalid() {
            result == -1
        }
    }

    @ToString(includePackage = false)
    static class ObjectQueryRes implements LivebosRes {

        Integer result
        String message

        boolean isSuccess() {
            result == 1
        }

        boolean isSessionInvalid() {
            result == -2
        }
    }

}
