package com.matrix.rpo.domain.crm

import com.matrix.rpo.domain.game.Project
import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@InitializeDomian(profiles = 'dev')
class Customer {
    String id
    String name
    String wechatId
    String cellPhone
    String email
    Date dateCreated
    Date lastUpdated

    static hasMany = [projects: Project]

    static constraints = {
        cellPhone unique: true
    }
    static graphql = GraphQLMapping.build {
        description "客户 - 一个客户可以建立多个游戏项目；通过后台进行维护，客户本身不需要登录系统"
        cellPhone description: '手机号 - 需唯一'
    }
    static
    final Customer TEST_CUSTOMER = new Customer(name: '测试客户', wechatId: 'aa', cellPhone: '132132132', email: 'aa@aa.com')
    static initList = [TEST_CUSTOMER]
}
