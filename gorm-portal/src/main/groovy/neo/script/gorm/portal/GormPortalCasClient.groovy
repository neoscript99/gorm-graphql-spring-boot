package neo.script.gorm.portal

import net.unicon.cas.client.configuration.CasClientConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnProperty(value = "portal.cas.client.enabled", havingValue = "true", matchIfMissing = false)
@Import(CasClientConfiguration.class)
class GormPortalCasClient {
}
