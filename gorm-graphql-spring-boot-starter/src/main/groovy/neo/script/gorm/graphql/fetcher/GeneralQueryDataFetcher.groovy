package neo.script.gorm.graphql.fetcher

import grails.gorm.PagedResultList
import graphql.schema.DataFetchingEnvironment
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import neo.script.gorm.general.util.GormCriteriaUtil
import neo.script.util.JsonUtil
import org.grails.gorm.graphql.fetcher.PaginatingGormDataFetcher
import org.grails.gorm.graphql.fetcher.impl.EntityDataFetcher
import org.grails.gorm.graphql.response.pagination.GraphQLPaginationResponseHandler
import org.grails.gorm.graphql.response.pagination.PagedResultListPaginationResponse
import org.hibernate.criterion.Projections

@InheritConstructors
@Slf4j
class GeneralQueryDataFetcher<T> extends EntityDataFetcher implements PaginatingGormDataFetcher {
    GraphQLPaginationResponseHandler responseHandler

    @Override
    protected T executeQuery(DataFetchingEnvironment environment, Map queryArgs) {
        if (!queryArgs.containsKey('max')) {
            queryArgs.put('max', responseHandler.defaultMax)
        }
        if (!queryArgs.containsKey('offset')) {
            queryArgs.put('offset', responseHandler.defaultOffset)
        }
        PagedResultList results =
                (PagedResultList) buildCriteria(environment).list(
                        queryArgs,
                        GormCriteriaUtil.makeCriteria(JsonUtil.fromJson(environment.getArgument('criteria'), Map))
                )
        (T) responseHandler.createResponse(environment, new PagedResultListPaginationResponse(results))
    }
}
