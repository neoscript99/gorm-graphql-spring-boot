package ns.gflex

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
class GflexBootApplication {

    static void main(String[] args) {
        SpringApplication.run(GflexBootApplication.class, args);
    }
}
