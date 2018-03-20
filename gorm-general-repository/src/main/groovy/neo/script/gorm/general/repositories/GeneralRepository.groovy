package neo.script.gorm.general.repositories

/**
 * 数据库操作通用接口
 * <p>本接口中用到的查询条件通过map来传递，map的key为数据库操作符（可嵌套），value为操作数（多个的话用list）
 * <p>例1，查找name为c2的记录：
 * <pre>[
 *         eq:[['name','c2']],
 * ]</pre>
 * <p>例2，查找name为c1或c2,且type.code like 'm%',并按照name降序取最前面10条记录：
 * <pre>[
 *         or:[eq:[['name','c2'],['name','c1']]],
 *       type:[like:[['code','m%']]],
 * firstResult:[101],
 * maxResults:[10],
 * 		order:[['name','desc']]
 * ]</pre>
 *
 * Created by Neo on 2017-09-01.
 * (<a href="http://docs.grails.org/latest/ref/Domain%20Classes/createCriteria.html">参考gorm实现，将map转化为gorm的方法调用</a.>)
 * @see grails.orm.HibernateCriteriaBuilder
 */
interface GeneralRepository {

    /**
     * 通过ID获取
     * @param id
     * @param domain
     * @return
     */
    public <T> T get(Class<T> domain, Object id)

    /**
     * 返回查询结果第一个元素，根据唯一键查询时有用
     * @param param
     * @param domain
     * @return
     */
    public <T> T findFirst(Class<T> domain, Map param)

    /**
     * query by example
     * 不支持属性嵌套
     * @param example
     * @return
     */
    public <T> List<T> findByExample(T example)

    /**
     * 通过map生成domain，并保存，同时支持insert和update，根据是否包含id字段自动执行
     * @param map
     * @param isMerge
     * @param domain
     * @return
     */
    public <T> T saveMap(Class<T> domain, Map map)

    /**
     * 根据map参数进行通用查询
     * @param params 参数
     * @param domain orm实体类--字符串或者Class
     * @return 结果列表
     */
    public <T> List<T> list(Class<T> domain, Map param)

    /**
     * 查询所有记录
     * @param domain
     * @return
     */
    public <T> List<T> listAll(Class<T> domain)

    /**
     * 通用计数函数
     * @param param 参数map
     * @param domain 实体--字符串或者Class
     * @return
     */
    int count(Class domain, Map param)

    int countAll(Class domain)

    /**
     * 保存domain实例到数据库，同时支持insert和update，根据是否包含id字段自动执行
     * @param entity
     * @return
     */
    Object saveEntity(Object entity)

    /**
     * update离线(id存在)实体类必须使用本方法
     * @param entity
     * @return
     */
    Object saveTransietEntity(Object entity)

    /**
     * 删除单个记录
     * @param entity
     */
    void delete(Object entity)

    /**
     * 根据id删除对应的记录
     * @param domain
     * @param id
     * @return 操作记录数
     */
    Number deleteById(Class domain, Object id)

    /**
     * 根据id列表删除对应的记录
     * @param domain
     * @param id
     * @return 操作记录数
     */
    Number deleteByIds(Class domain, List idList)

    /**
     * 删除param匹配的记录
     * @param domain
     * @param param
     * @return 操作记录数
     */
    Number deleteMatch(Class domain, Map param)

    /**
     * 更新param匹配的记录
     * @param domain
     * @param param
     * @param properties 更新内容
     * @return 操作记录数
     */
    Number updateMatch(Class domain, Map param, Map properties)

    /**
     * 将内存中的实例同步到数据库
     * @return
     */
    def flush()

    /**
     * 执行语句
     * <p>可以是sql、hql、jpql
     * @param ql
     * @param param
     * @return 更新记录数
     */
    Integer executeUpdate(String ql, Map<String, Object> param)
}
