package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity

@Entity
class ProgressScene {
    String id
    static belongsTo = [progress: Progress, scene: Scene]
}
