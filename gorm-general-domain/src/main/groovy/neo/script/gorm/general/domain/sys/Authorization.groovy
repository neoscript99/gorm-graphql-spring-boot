package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@InitializeDomian
class Authorization {
    String id
    //include exclude
    String authType
    /**
     * @see Token#userType
     */
    String userType
    String role
    //domain name | other
    String resource
    //self | org | all
    String region
    Boolean query
    Boolean mutation
    Date lastUpdated

    static initList = []
}
