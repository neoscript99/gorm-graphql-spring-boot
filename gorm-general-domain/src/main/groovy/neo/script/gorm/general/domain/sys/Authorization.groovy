package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@InitializeDomian
/**
 * 权限控制表
 */
class Authorization {
    String id
    //include exclude
    String authType
    /**
     * @see Token#userType
     */
    String userType
    String roleName
    //domain name | other
    String resourceName
    //self | org | all
    String regionName
    Boolean queryAllow
    Boolean mutationAllow
    Date lastUpdated

    static initList = []
}
