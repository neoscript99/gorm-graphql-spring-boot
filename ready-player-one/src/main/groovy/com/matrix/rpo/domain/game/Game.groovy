package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

import java.time.LocalDateTime

/**
 * 游戏
 */
@Entity
@InitializeDomian(profiles = 'dev', depends = Project)
class Game {
    String id
    Project project
    LocalDateTime beginTime
    LocalDateTime endTime
    Integer totalScenes

    Date dateCreated
    Date lastUpdated

    static hasMany = [players: Player, gameScenes: GameScene]
    static mapping = {
        project fetch: 'join', lazy: false
    }

    static graphql = GraphQLMapping.build {
        description '单轮游戏'
        beginTime description: '游戏开始时间'
        endTime description: '游戏结束时间'
    }

    static Game TEST_GAME = new Game(
            project: Project.TEST_PROJECT,
            beginTime: LocalDateTime.now(),
            endTime: LocalDateTime.now().plusDays(2),
            totalScenes: 5)
    static initList = [TEST_GAME]
}
