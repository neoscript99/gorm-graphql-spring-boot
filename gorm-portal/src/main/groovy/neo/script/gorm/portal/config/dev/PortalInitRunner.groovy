package neo.script.gorm.portal.config.dev

import neo.script.gorm.general.initializer.AbstractDataInitializerRunner
import neo.script.gorm.general.initializer.InitializeOrder
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(InitializeOrder.SECOND)
@Profile('dev')
class PortalInitRunner extends AbstractDataInitializerRunner {
}

