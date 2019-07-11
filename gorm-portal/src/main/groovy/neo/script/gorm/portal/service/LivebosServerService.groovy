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
        checkResult(restTemplate.getForObject(url, String.class, userId, livebosServer.sessionId), UserInfoRes, "getUserInfo($userId)")
    }


    UserInfoFull getUserInfoParse(LivebosServer livebosServer, String userId) {
        return JsonUtil.fromJson(getUserInfo(livebosServer, userId), UserInfoFull)
    }

    def queryNotices(String livebosServerId, String userId, String type) {
        queryNotices(get(livebosServerId), userId, type)
    }

    def queryNotices(LivebosServer livebosServer, String userId, String type) {
        def url = livebosServer.serverRoot + livebosServer.restPath + livebosServer.noticeUri
        checkResult(restTemplate.getForObject(url, String.class, userId, livebosServer.sessionId, type), NoticeRes, "queryNotices($userId, $type)")
    }

    /**
     * 默认用livebosQuery.condition做查询条件，如果有参数需要带入，可自行处理下
     * @param livebosQuery
     * @param condition
     * @return
     */
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
        if (livebosQuery.params)
            requestData.params = JsonUtil.fromJson(livebosQuery.params, Map)

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add('sessionId', livebosServer.sessionId)
        parts.add('requestData', requestData)

        return checkResult(restTemplate.postForObject(url, parts, String.class), ObjectQueryRes, "objectQuery(${livebosQuery})")
    }

    LivebosObject objectQueryParse(LivebosQuery livebosQuery) {
        def lb = JsonUtil.fromJson(objectQuery(livebosQuery), LivebosObject)
        if (lb.records && lb.records.size() > 0) {
            def colInfo = lb.metaData.colInfo
            lb.data = lb.records.collect { record ->
                def map = [:]
                record.eachWithIndex { value, int idx ->
                    map.put(colInfo.get(idx).name, value)
                }
                return map
            }
        }
        return lb
    }

    String checkResult(String resString, Class<? extends LivebosRes> resClass, String reqInfo) {
        LivebosRes res = JsonUtil.fromJson(resString, resClass)
        if (res.isSessionInvalid())
            refershSessionId()
        if (!res.isSuccess())
            log.error('LiveBOS远程调用失败，请求信息：{}，返回信息：{}', reqInfo, resString)
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

    /**
     * 仅查看结果状态
     */
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

    /**
     * 全部信息，后台有用的时候解析
     *{"id":0,"loginId":"admin","name":"管理员","lastLogin":"2019-07-02 16:48:48","grade":0,"status":1,"orgId":0}*/
    @ToString(includePackage = false)
    static class UserInfoFull extends UserInfoRes {
        Integer id
        String loginId
        String name
        String lastLogin
        Integer grade
        Integer orgId
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

    @ToString(includePackage = false)
    static class LivebosObjectColInfo {
        String name
        String label
        Integer type
        Integer length
        Integer scale
    }

    @ToString(includePackage = false)
    static class LivebosObjectMetaData {
        Integer colCount
        List<LivebosObjectColInfo> colInfo
    }

    @ToString(includePackage = false)
    static class LivebosObject extends ObjectQueryRes {
        String queryId
        Boolean hasMore
        Integer count
        LivebosObjectMetaData metaData
        List<List> records
        List data
    }
}
