package neo.script.gorm.graphql.demo.graphql

import neo.script.gorm.graphql.security.DomainAuthorization
import org.springframework.stereotype.Component

@Component
class DemoDomainAuthorization implements DomainAuthorization {
    boolean authorization(String token, Class domain, boolean isMutation) {
        if (token == 'bb')
            return true
        return false
    }
}
