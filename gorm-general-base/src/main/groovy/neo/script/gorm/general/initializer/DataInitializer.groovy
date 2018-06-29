package neo.script.gorm.general.initializer

import neo.script.gorm.general.repositories.GeneralRepository
import org.springframework.context.ApplicationContext;

/**
 * DataInitializer方式数据初始化接口类
 * <p>必须直接实现这个接口，并通过包扫描执行
 * <p>通过org.springframework.core.annotation.Order实现顺序执行
 * Created by Neo on 2017-08-22.
 */

interface DataInitializer {

    /**
     * 检查是否需要初始化，一般查询下数据库是否已有记录
     *
     * @return
     */
    boolean isInited();

    /**
     * 初始化内容
     */
    void doInit();

    /**
     * 判断并执行初始
     */
    void init()

    /**
     * 初始化过程中保存实例到数据库
     * @param entity
     * @return
     */
    def save(def entity)

    /**
     * 初始化数据库
     * @param generalRepository
     */
    void setGeneralRepository(GeneralRepository generalRepository)

    void setApplicationContext(ApplicationContext applicationContext)

}
