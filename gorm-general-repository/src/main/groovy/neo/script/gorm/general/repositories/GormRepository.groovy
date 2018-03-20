package neo.script.gorm.general.repositories

import grails.gorm.DetachedCriteria
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import neo.script.util.JsonUtil
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.model.PersistentEntity
import org.hibernate.SessionFactory
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.criterion.Projections
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.validation.FieldError

import java.text.MessageFormat

/**
 * 替换原来的GForm
 * <p>通过set到业务service，符合组合优于继承的设计规范，分离业务逻辑和数据库操作，为单元测试提供支持
 * <p>不涉及业务处理
 * Created by Neo on 2017-08-25.
 */
@Service
@Slf4j
@CompileStatic(TypeCheckingMode.SKIP)
class GormRepository implements GeneralRepository {
    @Autowired
    SessionFactory sessionFactory
    /**
     * @see GeneralRepository#get
     */
    @Override
    public <T> T get(Class<T> domain, def id) {
        domain.get(id)
    }

    /**
     * @see GeneralRepository#findFirst
     */
    @Override
    public <T> T findFirst(Class<T> domain, Map param) {
        List results = list(domain, param)
        if (results)
            results.first()
        else
            null
    }

    /**
     * @see GeneralRepository#findByExample
     */
    @Override
    public <T> List<T> findByExample(T example) {
        example.class.findAll(example)
    }

    /**
     * @see GeneralRepository#saveMap(Class, Map)
     */
    @Override
    public <T> T saveMap(Class<T> domain, Map map) {
        GormEntity newEntity = JsonUtil.mapToBean(map, domain)
        if (newEntity.ident())
            saveTransietEntity(newEntity)
        else
            saveEntity(newEntity)
    }

    /**
     * @see GeneralRepository#saveTransietEntity
     */
    Object saveTransietEntity(Object entity) {
        log.debug("saveTransietEntity $entity")
        GormEntity transietEntity = entity as GormEntity;
        GormEntity attachedEntity = transietEntity.class.get(transietEntity.ident())

        if (attachedEntity) {//update
            if (attachedEntity.hasProperty('version'))
                log.debug("old version is $attachedEntity.version")

            BeanUtils.copyProperties(transietEntity, attachedEntity,
                    ['id', 'version', 'metaClass'].toArray(new String[0]))
            saveEntity(attachedEntity)
        } else //insert
            saveEntity(transietEntity)
    }

    /**
     * @see GeneralRepository#list
     */
    @Override
    public <T> List<T> list(Class<T> domain, Map param) {
        log.info "list - ${domain} param:$param"

        if (param?.projections) {
            log.info "resultTransformer to CriteriaSpecification.ALIAS_TO_ENTITY_MAP"
            param.put 'resultTransformer', [
                    CriteriaSpecification.ALIAS_TO_ENTITY_MAP
            ]
        }

        criteriaQuery(domain, param, false)
    }

    /**
     * @see GeneralRepository#listAll
     */
    @Override
    public <T> List<T> listAll(Class<T> domain) {
        list(domain, null)
    }
    /**
     * @see GeneralRepository#count
     */
    @Override
    int count(Class domain, Map param) {
        log.info "count - ${domain} param:$param"
        criteriaQuery(domain, param, true)
    }

    /**
     * @see GeneralRepository#countAll
     */
    @Override
    int countAll(Class domain) {
        count(domain, null)
    }

    /**
     * @see GeneralRepository#delete
     */
    @Override
    void delete(Object entity) {
        entity.delete()
    }

    /**
     * @see GeneralRepository#deleteById
     */
    @Override
    Number deleteById(Class domain, Object id) {
        deleteByIds(domain, [id])
    }

    /**
     * @see GeneralRepository#deleteByIds
     */
    @Override
    Number deleteByIds(Class domain, List idList) {
        deleteMatch(domain, ['in': [['id', idList]]])
    }

    /**
     * @see GeneralRepository#deleteMatch
     */
    @Override
    Number deleteMatch(Class domain, Map param) {
        def criteria = buildDetachedCriteria(domain, param)
        criteria ? criteria.deleteAll() : 0
    }

    /**
     * @see GeneralRepository#updateMatch
     */
    @Override
    Number updateMatch(Class domain, Map param, Map properties) {
        def criteria = buildDetachedCriteria(domain, param)
        criteria ? criteria.updateAll(properties) : 0
    }

    /**
     * @see GeneralRepository#saveEntity(Object)
     */
    @Override
    def saveEntity(Object entity) {
        log.info "saveEntity  ${entity.class}"

        GormEntity gormEntity = entity as GormEntity;
        //错误信息可能在中间过程中产生，末状态正确时不一定为空，所以必须先清理
        gormEntity.clearErrors()
        if (gormEntity.validate()) {
            /* 如果存在unique限制，当save为update时导致NonUniqueObjectException异常
             * 原因是查询是否唯一时，导致session有两个id相同对象，grails目前处理不够好。
             * 如果用entity.merge()，部分功能，如autoTimeStrape无法自动完成
             * 也可通过清空session处理
             * 20170929 新版gorm中没有发现这个问题，isMerge暂不使用
             *
			 * if(isMerge){
			 * 	entity.merge()
		     * }
             * */
            log.debug "saveEntity ${gormEntity}"
            gormEntity.save()
        } else {
            StringBuilder sb = new StringBuilder()
            gormEntity.errors.allErrors.each {
                String msg = it.defaultMessage ? MessageFormat.format(it.defaultMessage, it.arguments) : it.toString();
                if (FieldError.isAssignableFrom(it.class))
                    msg = "[$it.field = $it.rejectedValue]$msg"
                log.error(msg)
                log.error(it.toString())
                sb.append("$msg\n")
                //messageBundle配置参考log.error(it)打印的error.code，
                //如com.beeb.teller.TeTransInfo.transCode.unique.error
                //sb.append("${messageSource.getMessage(it, Locale.default)}\n")

            }
            throw new RuntimeException(sb.toString())
        }
    }

    /**
     * @see GeneralRepository#flush
     */
    def flush() {
        sessionFactory.currentSession.flush()
    }

    /**
     * @see GeneralRepository#executeUpdate
     */
    Integer executeUpdate(String ql, Map<String, Object> param) {
        def query = sessionFactory.currentSession.createQuery(ql)
        param.each { query.setParameter(it.key, it.value) }
        query.executeUpdate()
    }
    /**
     * 使用hibernate criteria 查询
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @param domainClass
     * @param param
     * @param isCount 是否计数
     * @return
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected Object criteriaQuery(Class domainClass, Map param, boolean isCount) {
        def criteria = domainClass.createCriteria()
        if (isCount)
            criteria.count(makeCriteria(param))
        else
            criteria.list(makeCriteria(param))
    }

    /**
     * 将map参数转化为HibernateCriteriaBuilder
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected def makeCriteria(Map param) {
        return {
            param.each { k, v ->
                log.debug "invokeMethod $k($v)"
                //如果v是Map，递归makeQuery
                if (v instanceof Map)
                    invokeMethod k, makeCriteria(v)
                //否则v必须为list，将list的每个值作为参数数组，循环调用Builder的函数
                else
                    v.each {
                        def args = (it instanceof List) ? it.toArray() : it

                        //通过id查询不能自动转换Integer为Long(已通过修改org.grails.datastore.mapping.query.jpa.JpaQueryBuilder修复)
                        if (k == 'eq' && (args[0] =~ '.*[iI]d$') && args[1] instanceof Integer)
                            args[1] = args[1].longValue()
                        //HibernateCriteriaBuilder不支持sqlGroupProjection
                        if (k == 'sqlGroupProjection')
                            addProjectionToList(Projections.invokeMethod('sqlGroupProjection', args), args[2][0])
                        else if (k == 'sqlProjection')
                            addProjectionToList(Projections.invokeMethod('sqlProjection', args), args[1][0])
                        else
                            invokeMethod k, args
                    }
            }
        }
    }
    /**
     * 创建criteria并执行操作
     *
     * <p>适合批量删除等操作
     * <p>本方法为hibernate专用，如需使用需继承本类
     * <p>批量update、delete不支持join，gorm的DetachedCriteria暂时也不支持in和exists子查询，所以先分两步执行
     * @param domain
     * @param param
     * @return 没有满足条件的记录，返回null，调用方无需执行，否则返回in查询的DetachedCriteria
     * @see grails.orm.HibernateCriteriaBuilder
     */
    protected DetachedCriteria buildDetachedCriteria(Class<GormEntity> domain, Map param) {
        log.info("$domain : $param")

        def detachedCriteria = new DetachedCriteria(domain)
        if (param) {
            def subQuery = new DetachedCriteria(domain).build(makeCriteria(param)).id();
            def idList = subQuery.list();
            //如果子查询没有匹配记录，返回null，调用方无需执行
            return idList ? detachedCriteria.inList(getIdName(domain), idList) : null
        } else //批量操作必需包含where子句，所以加一个必真的条件
            return detachedCriteria.isNotNull(getIdName(domain))
    }


    static getIdName(Class<GormEntity> domain) {
        (domain.gormPersistentEntity as PersistentEntity).identity.name
    }
}
