package com.matrix.rpo.domain.game

import com.matrix.rpo.domain.crm.Account
import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.datastore.gorm.GormEntity

@Entity
@InitializeDomian(profiles = 'dev', depends = [Account, Game])
class Player implements GormEntity<Player> {
    String id
    String name
    String faceId

    static belongsTo = [account: Account, game: Game]

    static mapping = {
        account fetch: 'join', lazy: false
        game fetch: 'join', lazy: false
    }
    Date dateCreated
    Date lastUpdated

    /**
     * @see com.matrix.rpo.graphql.mapping.PlayerGraphqlMapping
     */
    static graphql
    static PLAYER_ONE = new Player(name: "头号玩家", faceId: 'aabbcc', account: Account.TEST_ACCOUNT, game: Game.TEST_GAME)
    static initList = [PLAYER_ONE]
}
