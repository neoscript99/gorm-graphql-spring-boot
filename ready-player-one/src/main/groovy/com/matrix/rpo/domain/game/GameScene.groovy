package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
class GameScene {
    String id
    Game game
    Scene scene

    static mapping = {
        game lazy: false, fetch: 'join'
        scene lazy: false, fetch: 'join'
    }

    static graphql = GraphQLMapping.build {
        description '单轮游戏包含的场景'
    }
}
