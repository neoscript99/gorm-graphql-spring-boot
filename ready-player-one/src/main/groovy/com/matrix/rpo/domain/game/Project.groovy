package com.matrix.rpo.domain.game

import com.matrix.rpo.domain.crm.Customer
import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

/**
 * 游戏项目
 *
 * 一个游戏项目对应一个训练模型
 * 一个项目可以多次发起活动
 */
@Entity
class Project {
    String id
    String name
    String modelId
    Customer customer

    Date dateCreated
    Date dateUpdated

    static graphql = GraphQLMapping.build {
        description: "一个游戏项目，对应一个训练模型，包含多个识别场景，可多次发起游戏"
    }
}
