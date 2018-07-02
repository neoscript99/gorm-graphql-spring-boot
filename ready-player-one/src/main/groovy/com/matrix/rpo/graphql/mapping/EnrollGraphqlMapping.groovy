package com.matrix.rpo.graphql.mapping

import com.matrix.rpo.domain.game.Enroll
import com.matrix.rpo.service.EnrollService
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@GraphQLMappingFlag(Enroll)
class EnrollGraphqlMapping extends GraphQLMapping {
    @Autowired
    EnrollService enrollService

    EnrollGraphqlMapping() {
        description '游戏的报名信息，需要上传能检测出单个人脸的图片'
        property('faceId') {
            description: "一个player对应一个微信号，但每次报名参与的人脸图片可以换"
        }
        mutation('enroll', 'EnrollInfo') {
            description '游戏报名'
            argument('gameId', String)
            argument('playerId', String)
            argument('faceId', String)

            dataFetcher(new EnrollDataFetcher())
            returns {
                field('success', Boolean)
                field('error', String)
            }
        }
    }

    class EnrollDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            def result = enrollService.enroll(environment.getArgument('gameId'),
                    environment.getArgument('playerId'),
                    environment.getArgument('faceId'));
            if (result.success)
                [success: true]
            else
                result
        }
    }
}
