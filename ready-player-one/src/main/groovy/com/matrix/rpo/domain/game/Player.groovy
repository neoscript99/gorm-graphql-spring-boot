package com.matrix.rpo.domain.game

import com.matrix.rpo.domain.crm.Account
import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class Player implements GormEntity<Player> {
    String id
    String name
    String faceId

    Account account
    Game game

    Date dateCreated
    Date dateUpdated

    /**
     * @see com.matrix.rpo.graphql.mapping.PlayerGraphqlMapping
     */
    static graphql
}
