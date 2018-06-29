package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity

/**
 * 游戏
 */
@Entity
class Game {
    String id
    Project project
    Date beginTime
    Date endTime
}
