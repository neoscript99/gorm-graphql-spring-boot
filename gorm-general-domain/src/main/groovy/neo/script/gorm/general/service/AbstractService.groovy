package neo.script.gorm.general.service

import neo.script.gorm.general.repositories.GeneralRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * service基础类(GFLexService重构)
 * 创建时间 2018-06-06
 * @author wangchu
 */
@Transactional
abstract class AbstractService<T> {
    @Autowired
    GeneralRepository generalRepository

    protected Logger log = LoggerFactory.getLogger(this.getClass())
    private Class<T> domain;

    public AbstractService() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        domain = (Class) params[0];
    }

    @Transactional(readOnly = true)
    public T get(def id) {
        generalRepository.get(domain, id)
    }
    /**
     * 根据param查询domain
     * @param param
     * @return
     */
    @Transactional(readOnly = true)
    List<T> list(Map param = null) {
        if (defaultOrder) {
            param = param ?: [:]
            param.order = param.order ?: defaultOrder
        }
        generalRepository.list domain, param
    }

    @Transactional(readOnly = true)
    List<T> findByIds(List idList) {
        list(['in': [['id', idList]]])
    }

    @Transactional(readOnly = true)
    List<T> findByIds(Serializable[] ids) {
        list(['in': [['id', ids]]])
    }

    @Transactional(readOnly = true)
    T findFirst(Map param = null) {
        generalRepository.findFirst(domain, param)
    }

    @Transactional(readOnly = true)
    List listEnabled(Map param = null) {
        log.info "listEnabled param:$param"
        listWithEq(['enabled', true], param)
    }

    @Transactional(readOnly = true)
    List listWithEq(List eqParam, Map param = null) {
        if (param)
            param.eq = param.eq ? param.eq.toList() << eqParam : [eqParam]
        else
            param = [eq: [eqParam]]
        list(param)
    }

    def save(Map map) {
        log.debug("save map: {}", map)
        generalRepository.saveMap domain, map
    }

    T saveEntity(T entity) {
        generalRepository.saveEntity(entity)
    }

    @Transactional(readOnly = true)
    int count(Map param = null) {
        generalRepository.count domain, param
    }

    @Transactional(readOnly = true)
    int countDistinct(Map param = null, List<String> propertyList) {
        def projections = [projections: [countDistinct: propertyList]]
        generalRepository.findFirst(projections << param, domain).countDistinct;
    }

    /**
     * @see GeneralRepository#deleteByIds
     */
    Number deleteById(def id) {
        generalRepository.deleteById(domain, id)
    }
    /**
     * @see GeneralRepository#deleteByIds
     */
    Number deleteByIds(List idList) {
        log.info("deleteByIds: $idList")
        if (idList)
            generalRepository.deleteByIds(domain, idList)
    }

    /**
     * @see GeneralRepository#deleteMatch
     */
    Number deleteMatch(Map param) {
        generalRepository.deleteMatch(domain, param)
    }

    /**
     * @see GeneralRepository#updateMatch
     */
    Number updateMatch(Map param, Map properties) {
        generalRepository.updateMatch(domain, param, properties)
    }

    List getDefaultOrder() {
        null
    }
}
