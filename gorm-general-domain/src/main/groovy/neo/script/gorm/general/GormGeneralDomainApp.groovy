package neo.script.gorm.general

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class GormGeneralDomainApp {

    static void main(String[] args) {
        SpringApplication.run(GormGeneralDomainApp.class, args);
    }
}
