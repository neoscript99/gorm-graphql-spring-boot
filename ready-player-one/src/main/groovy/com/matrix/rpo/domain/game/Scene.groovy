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
    static hasMany = [gameProgresses: Progress]

    static graphql = GraphQLMapping.build {
        description "游戏场景 - Project包含多个Scene，Game发起时可选取enabled=true的多个"
        code description: '场景代码，需和训练模型做对应'
        project description: '场景代码，需和训练模型做对应'
    }
}
