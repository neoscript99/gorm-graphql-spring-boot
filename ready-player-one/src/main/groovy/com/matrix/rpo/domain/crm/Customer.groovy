package com.matrix.rpo.domain.crm

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
class Customer {
    String id
    String name
    String cellPhone
    String email
    Date dateCreated
    Date dateUpdated

    static constraints = {
        cellPhone unique: true
    }
    static graphql = GraphQLMapping.build {
        description "客户 - 一个客户可以建立多个游戏项目；通过后台进行维护，客户本身不需要登录系统"
        modelId description: '训练模型，对应到外部系统'
        cellPhone description: '手机号 - 需唯一'
    }
}
