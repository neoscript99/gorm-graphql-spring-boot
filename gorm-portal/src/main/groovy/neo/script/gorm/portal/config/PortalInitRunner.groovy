package neo.script.gorm.portal.config

import neo.script.gorm.general.initializer.AbstractDataInitializerRunner
import neo.script.gorm.general.initializer.InitializeOrder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(InitializeOrder.SECOND)
class PortalInitRunner extends AbstractDataInitializerRunner {
}

