package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
class Progress {
    String id
    Integer markedNumber
    Boolean complete
    Date lastMarkTime

    static belongsTo = [player: Player]
    static hasMany = [progressScenes: ProgressScene]

    static mapping = {
        player fetch: 'join', lazy: false
    }

    static graphql = GraphQLMapping.build {
        description "游戏进度"
        markedNumber description: '完成的场景数'
        lastMarkTime description: '最后一个场景完成的时间'
    }
}
