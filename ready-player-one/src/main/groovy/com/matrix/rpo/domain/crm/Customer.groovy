package com.matrix.rpo.domain.crm

import grails.gorm.annotation.Entity

/**
 * 客户
 *
 * 一个客户可以建立多个游戏项目
 */
@Entity
class Customer {
    String id
    String name
    String cellPhone
    String email
    Date dateCreated
    Date dateUpdated

    static graphql = true
}
