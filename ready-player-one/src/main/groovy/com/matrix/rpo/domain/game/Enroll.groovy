package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity

@Entity
class Enroll {
    String id
    Game game
    Player player
    String faceId

    Date dateCreated
    Date dateUpdated

    static constraints = {
        game unique: 'player'
    }

    static graphql
}
