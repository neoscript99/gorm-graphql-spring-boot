package neo.script.gorm.graphql.fetcher

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import static org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType.*
import org.grails.gorm.graphql.fetcher.ReadingGormDataFetcher
import org.grails.gorm.graphql.fetcher.manager.DefaultGraphQLDataFetcherManager

@InheritConstructors
@CompileStatic
class GormGraphQLDataFetcherManager extends DefaultGraphQLDataFetcherManager {

    @Override
    Optional<ReadingGormDataFetcher> getReadingFetcher(PersistentEntity entity, GraphQLDataFetcherType type) {
        if (type == LIST)
            Optional.of(new CriteriaDataFetcher(entity)) as Optional<ReadingGormDataFetcher>
        else
            super.getReadingFetcher(entity, type)
    }
}
