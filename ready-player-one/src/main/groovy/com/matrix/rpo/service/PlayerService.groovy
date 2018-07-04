package com.matrix.rpo.service

import com.matrix.rpo.domain.game.Player
import neo.script.gorm.general.service.AbstractService
import org.springframework.stereotype.Service

@Service
class PlayerService extends AbstractService<Player> {
    def enroll(String gameId, String playerId, String faceId) {

    }
}
