package neo.script.gorm.portal.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.portal.domain.pt.ds.LivebosServer
import neo.script.gorm.portal.service.LiveServerService
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(LivebosServer)
class LiveServerMapping extends GraphQLMapping {
    @Autowired
    LiveServerService liveServerService

    LiveServerMapping() {
        query('liveGetUserInfo', String) {
            argument('liveServerId', String)
            argument('userId', String)
            dataFetcher(new GetUserInfoDataFetcher())
        }
        query('liveQueryNotices', String) {
            argument('liveServerId', String)
            argument('userId', String)
            argument('type', String)
            dataFetcher(new QueryNoticesDataFetcher())
        }
        query('liveObjectQuery', String) {
            argument('liveQueryId', String)
            dataFetcher(new ObjectQueryDataFetcher())
        }
    }


    class GetUserInfoDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return liveServerService.getUserInfo(
                    environment.<String> getArgument('liveServerId'),
                    environment.<String> getArgument('userId'))
        }
    }


    class QueryNoticesDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return liveServerService.queryNotices(
                    environment.<String> getArgument('liveServerId'),
                    environment.<String> getArgument('userId'),
                    environment.<String> getArgument('type'))
        }
    }

    class ObjectQueryDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return liveServerService.objectQuery(
                    environment.<String> getArgument('liveQueryId'))
        }
    }
}
