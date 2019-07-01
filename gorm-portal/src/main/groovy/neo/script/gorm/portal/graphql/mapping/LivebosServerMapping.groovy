package neo.script.gorm.portal.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
import neo.script.gorm.portal.service.LivebosServerService
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(LivebosServer)
class LivebosServerMapping extends GraphQLMapping {
    @Autowired
    LivebosServerService livebosServerService

    LivebosServerMapping() {
        query('livebosGetUserInfo', String) {
            argument('livebosServerId', String)
            argument('userId', String)
            dataFetcher(new GetUserInfoDataFetcher())
        }
        query('livebosQueryNotices', String) {
            argument('livebosServerId', String)
            argument('userId', String)
            argument('type', String)
            dataFetcher(new QueryNoticesDataFetcher())
        }
    }

    class GetUserInfoDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return livebosServerService.getUserInfo(
                    environment.<String> getArgument('livebosServerId'),
                    environment.<String> getArgument('userId'))
        }
    }

    class QueryNoticesDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return livebosServerService.queryNotices(
                    environment.<String> getArgument('livebosServerId'),
                    environment.<String> getArgument('userId'),
                    environment.<String> getArgument('type'))
        }
    }
}
