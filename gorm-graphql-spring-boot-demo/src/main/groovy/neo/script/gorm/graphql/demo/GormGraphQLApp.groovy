package neo.script.gorm.graphql.demo

import de.codecentric.boot.admin.config.EnableAdminServer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableAdminServer
class GormGraphQLApp {

    static void main(String[] args) {
        SpringApplication.run(GormGraphQLApp.class, args);
    }
}
