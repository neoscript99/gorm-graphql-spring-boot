package neo.script.gorm.graphql.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class GormGraphQLApp {

    static void main(String[] args) {
        SpringApplication.run(GormGraphQLApp.class, args);
    }
}
