package neo.script.gorm.graphql.demo.config

import de.codecentric.boot.admin.config.EnableAdminServer
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
@EnableAdminServer
class ProfileConfig {
}
