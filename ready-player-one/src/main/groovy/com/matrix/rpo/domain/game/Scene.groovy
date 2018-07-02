package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping


@Entity
class Scene {
    String id
    String name
    String code
    Boolean enabled
    Project project
    static hasMany = [gameProgresses: GameProgress]

    static graphql = GraphQLMapping.build {
        description "一个游戏项目可以设定多个识别场景，每次游戏可以选取其中多个"
        code description: '场景代码，和训练模型做对应'
    }
}
