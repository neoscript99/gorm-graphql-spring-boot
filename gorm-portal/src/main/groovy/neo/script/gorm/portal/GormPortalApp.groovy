package neo.script.gorm.portal

import net.unicon.cas.client.configuration.EnableCasClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
//@EnableCasClient
class GormPortalApp {
    static void main(String[] args) {
        SpringApplication.run(GormPortalApp.class, args);
    }
}
