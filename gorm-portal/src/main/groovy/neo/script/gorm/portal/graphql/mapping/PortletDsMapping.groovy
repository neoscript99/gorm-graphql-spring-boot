package neo.script.gorm.portal.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.portal.domain.pt.ds.PortletDs
import neo.script.gorm.portal.service.PortletDsService
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(PortletDs)
class PortletDsMapping extends GraphQLMapping {
    @Autowired
    PortletDsService portletDsService

    PortletDsMapping() {
        query('portletData', [String]) {
            argument('dsId', String)
            dataFetcher(new GetDataFetcher())
        }
    }


    class GetDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return portletDsService.getData(environment.getArgument('dsId'))
        }
    }
}
