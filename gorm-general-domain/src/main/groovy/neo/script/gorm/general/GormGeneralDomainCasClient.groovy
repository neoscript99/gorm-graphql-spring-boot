package neo.script.gorm.general

import net.unicon.cas.client.configuration.CasClientConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnProperty(value = "gorm.cas.client.enabled", havingValue = "true", matchIfMissing = false)
@Import(CasClientConfiguration.class)
class GormGeneralDomainCasClient {
}
