package neo.script.gorm.general.config

import neo.script.gorm.data.initializer.initialize.AbstractDataInitializerRunner
import neo.script.gorm.data.initializer.initialize.InitializeOrder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


/**
 * DataInitializer方式的数据初始化
 * <p> 本runner需依赖 DomainInitRunner
 * <p> 基础系统初始化为最高优先级，如果其它模块依赖本模块的初始化数据，优先级必须低于本类
 * <p> 本模块初始化包括：MenuInitializer
 * <p> 通过命令行参数"--init"发起本初始化过程
 * @see neo.script.gorm.general.config.data.MenuInitializer
 * Created by Neo on 2017-07-01.
 */
@Component
@Order(InitializeOrder.FIRST)
class GormGeneralDomainInitRunner extends AbstractDataInitializerRunner {

}