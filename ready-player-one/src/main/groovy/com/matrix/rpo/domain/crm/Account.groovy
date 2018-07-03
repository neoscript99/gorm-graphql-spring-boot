package com.matrix.rpo.domain.crm

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
class Account {
    String id
    String name
    String account
    /**
     * 密码 - guava sha256加密
     */
    String password

    String cellPhone
    String wechatId
    String qqId

    Date dateCreated
    Date dateUpdated

    static constraints = {
        cellPhone unique: true
    }
    static graphql = GraphQLMapping.build {
        description "用户帐号 - 通过终端登录的用户帐号，包括微信、APP、网页等所有渠道"
        wechatId description: '微信的帐号、邮箱或手机'
        cellPhone description: '手机 - 唯一性'
        password description: '密码 - sha256加密'
    }
}
