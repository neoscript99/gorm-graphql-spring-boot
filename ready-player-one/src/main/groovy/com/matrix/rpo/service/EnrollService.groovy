package com.matrix.rpo.service

import com.matrix.rpo.domain.game.Enroll
import neo.script.gorm.general.service.AbstractService
import org.springframework.stereotype.Service

@Service
class EnrollService extends AbstractService<Enroll> {
    def enroll(String gameId, String playerId, String faceId) {

    }
}
