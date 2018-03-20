package neo.script.gorm.general.domain

import grails.gorm.annotation.Entity
import groovy.transform.ToString
import neo.script.gorm.data.initializer.initialize.InitializeDomian

@Entity
@ToString(includePackage = false, includes = 'name')
@InitializeDomian('deptList')
class Department {
    static final Department HEAD_OFFICE = new Department(name: '总部', seq: 1);
    String id
    String name;
    Integer seq;
    Boolean enabled = true

    static deptList = [HEAD_OFFICE]
    static graphql = true
}
