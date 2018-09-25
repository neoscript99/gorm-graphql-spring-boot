package ns.gflex.services.base

import groovy.sql.Sql
import neo.script.gorm.general.repositories.GeneralRepository
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.flex.remoting.RemotingDestination
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

/**
 * service基础类
 * 创建时间 2010-6-1
 * @author wangchu
 */
@RemotingDestination
@Transactional
abstract class GFlexService {
    protected Logger log = LoggerFactory.getLogger(this.getClass())
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    SessionFactory sessionFactory

    static final String SESSION_ACCOUNT_ID = 'AccountInFlexSession'

    protected Session getHibernateSession() {
        sessionFactory.currentSession
    }

    protected HttpServletRequest getHttpRequest() {
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected def getFlexSession() {
        //不是flex发起的request无法获取这个session
        //FlexContext.flexSession
        httpSession
    }

    protected HttpSession getHttpSession() {
        httpRequest.session
    }

    protected void logHost(String message, boolean isDebug = false) {
        String host = httpRequest ? httpRequest.remoteHost : '@server'
        if (isDebug)
            log.debug("$host $message")
        else
            log.info("$host $message")
    }

    void heartBeat() {
        logHost("send heart beat!", true)
    }
    /**
     * 得到groovy sql 实例
     * @return
     */
    protected Sql getGroovySql() {
        new Sql(sessionFactory.currentSession.connection())
    }

    def save(Map map, boolean isMerge = false, Object domain = null) {
        log.info("save map isMerge:{} , domain:{}", isMerge, domain)
        log.debug("save map: {}", map)
        authorityCheck()
        generalRepository.saveMap forDomain(domain), map//, isMerge 2017新版不再处理merge
    }

    @Transactional(readOnly = true)
    List listEnabled(Map param = null, Object domain = null) {
        log.info "listEnabled param:$param"
        if (param) {
            if (param.eq)
                param.eq = param.eq.toList() << ['enabled', true]
            else
                param.eq = [['enabled', true]]
        } else
            param = [eq: [['enabled', true]]]
        list(param, domain)
    }

    @Transactional(readOnly = true)
    List list(Map param = null, Object domain = null) {
        authorityCheck()
        generalRepository.list forDomain(domain), param
    }

    @Transactional(readOnly = true)
    int count(Map param = null, Object domain = null) {
        generalRepository.count forDomain(domain), param
    }

    /**
     * @param param ex.[projections:[countDistinct:[['customerId', 'countDistinct']]]]
     * @param domain
     * @return
     */
    @Transactional(readOnly = true)
    int countDistinct(Map param, Object domain) {
        list(param, domain)[0].countDistinct;
    }

    protected def saving(def entity, boolean isMerge = false) {
        authorityCheck()
        //2017新版不再处理是否merge  generalRepository.saveEntity(entity, isMerge)
        generalRepository.saveEntity(entity)
    }

    /**
     * 根据id删除关联信息
     * @entityArray 包含id属性的实体列表
     */
    void deleteByIds(List idList, def domain = null) {
        log.info("deleteByIds: $idList")
        if (idList)
            generalRepository.deleteByIds(forDomain(domain), idList)
    }

    /**
     * 默认的实体类
     * @return
     */
    abstract protected Class getDomainClass()

    protected Class forDomain(def domain) {
        if (!domain) return domainClass
        else if (domain instanceof Class)
            return domain
        else
            return Thread.currentThread().contextClassLoader.loadClass(domain.toString())
    }

    /**当前登录工号
     * @return
     */
    String getSessionAccount(def session = null) {
        String sessionAccount = (session ?: httpSession).getAttribute(SESSION_ACCOUNT_ID)
        log.info("current user is {}", sessionAccount)
        return sessionAccount
    }

    //某些工程没有ns.gflex.domain.User类，不能调用本方法
    def getSessionUser() {
        //防止编译错误
        if (sessionAccount)
            forDomain('neo.script.gorm.general.domain.sys.User').findByAccount(sessionAccount)
    }

    /**
     * 后台防恶意调用
     * @return
     */
    protected void authorityCheck() {
        //未登录时报错
        if (!sessionAccount) {
            logHost('Authority check failed!')
            throw new RuntimeException('登录超时，请刷新页面！')
        }
    }
    /**
     * 抛出RuntimeException异常
     * @param str
     * @return
     */
    protected throwError(String str) {
        throw new RuntimeException(str)
    }
}
