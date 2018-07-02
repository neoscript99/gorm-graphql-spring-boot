package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

/**
 *
 */
@Entity
class Player implements GormEntity<Player> {
    String id
    String name
    String wechatId

    Date dateCreated
    Date dateUpdated
}
