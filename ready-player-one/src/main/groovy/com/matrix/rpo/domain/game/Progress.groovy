package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
class Progress {
    String id
    Player player
    Integer markedNumber
    Boolean complete
    Date lastMarkTime
    static hasMany = [markedScenes: Scene]

    static graphql = GraphQLMapping.build{
        description "游戏进度"
        scoredNumber description:'完成的场景数'
        lastScoreTime description:'最后一个场景完成的时间'
    }
}
