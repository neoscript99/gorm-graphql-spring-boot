package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

/**
 * 游戏
 */
@Entity
class Game {
    String id
    Project project
    Date beginTime
    Date endTime
    Integer totalScenes

    Date dateCreated
    Date dateUpdated

    static graphql = GraphQLMapping.build {
        description '单轮游戏'
        beginTime description: '游戏开始时间'
        endTime description: '游戏结束时间'
    }
}
