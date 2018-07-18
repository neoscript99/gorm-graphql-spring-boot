package com.matrix.rpo.domain.crm

import com.matrix.rpo.domain.game.Player
import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.util.EncoderUtil
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@InitializeDomian(profiles = 'dev')
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
    Date lastUpdated

    static hasMany = [players: Player]

    static constraints = {
        cellPhone unique: true
        account unique: true
        password maxSize: 64
    }
    static graphql = GraphQLMapping.build {
        description "渠道用户帐号 - 通过终端登录的用户帐号，包括微信、APP、网页等所有渠道"
        property('wechatId') {
            description '微信的帐号、邮箱或手机'
        }
        cellPhone description: '手机 - 唯一性'
        password description: '密码 - sha256加密'
    }

    static TEST_ACCOUNT = new Account(name: '渠道测试用户', account: 'test001', password: EncoderUtil.sha256('test001')
            , cellPhone: '123456', wechatId: 'test001', qqId: 'test001')
    static initList = [TEST_ACCOUNT]
}
