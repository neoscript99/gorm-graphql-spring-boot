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

@InheritConstructors
@Slf4j
class CriteriaDataFetcher<T> extends EntityDataFetcher implements PaginatingGormDataFetcher {
    GraphQLPaginationResponseHandler responseHandler

    /**
     * @see org.grails.datastore.gorm.query.criteria.AbstractDetachedCriteria#max(int)
     * @param environment
     * @param queryArgs
     * @return
     */
    @Override
    protected T executeQuery(DataFetchingEnvironment environment, Map queryArgs) {
        def criteriaMap = environment.getArgument('criteria') ? JsonUtil.fromJson(environment.getArgument('criteria'), Map, false) : null
        //传入max，buildCriteria(environment).list才会返回PagedResultList
        //AbstractDetachedCriteria#max(int)返回新对象，
        //所以max,offset不能放到GormCriteriaUtil.makeCriteria(criteriaMap)的map中，需单独放到queryArgs
        if (!queryArgs.containsKey('max'))
            queryArgs.put('max', criteriaMap?.max ?: 1000)
        if (!queryArgs.containsKey('offset'))
            queryArgs.put('offset', criteriaMap?.offset ?: 0)

        PagedResultList results =
                (PagedResultList) buildCriteria(environment).list(
                        queryArgs,
                        GormCriteriaUtil.makeCriteria(criteriaMap)
                )
        (T) responseHandler.createResponse(environment, new PagedResultListPaginationResponse(results))
    }
}
