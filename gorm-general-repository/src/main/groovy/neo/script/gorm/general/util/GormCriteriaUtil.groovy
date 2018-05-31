package neo.script.gorm.general.util

import grails.gorm.DetachedCriteria
import groovy.util.logging.Slf4j
import org.grails.datastore.gorm.GormEntity
import org.hibernate.criterion.Projections

@Slf4j
class GormCriteriaUtil {
    /**
     * 将map参数转化为HibernateCriteriaBuilder
     * <p>本方法为hibernate专用，如需使用需继承本类
     * @see grails.orm.HibernateCriteriaBuilder
     */
    static def makeCriteria(Map param) {
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
    static DetachedCriteria buildDetachedCriteria(Class<GormEntity> domain, Map param) {
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
}
