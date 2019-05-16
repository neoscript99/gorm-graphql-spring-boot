package neo.script.gorm.portal.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.portal.domain.ptl.PortletTable
import neo.script.gorm.portal.service.PortletTableService
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(PortletTable)
class PortletTableMapping extends GraphQLMapping {
    @Autowired
    PortletTableService portletTableService

    PortletTableMapping() {
        exclude('dbQuery')
        query('portletTableData', [String]) {
            description 'get database query data'
            argument('portletTableId', String)
            dataFetcher(new GetDataFetcher())
        }
    }


    class GetDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return portletTableService.getData(environment.getArgument('portletTableId'))
        }
    }
}
