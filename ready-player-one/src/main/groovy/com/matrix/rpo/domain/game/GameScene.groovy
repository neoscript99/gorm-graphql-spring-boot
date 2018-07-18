package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@InitializeDomian(profiles = 'dev', depends = [Game, Scene])
class GameScene {
    String id
    static belongsTo = [game: Game, scene: Scene]

    static mapping = {
        game lazy: false, fetch: 'join'
        scene lazy: false, fetch: 'join'
    }

    static graphql = GraphQLMapping.build {
        description '单轮游戏包含的场景'
    }
    static initList = Scene.initList.collect {
        new GameScene(game: Game.TEST_GAME, scene: it)
    }

}
