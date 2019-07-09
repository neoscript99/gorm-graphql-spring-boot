package neo.script.gorm.portal.graphql.mapping

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import neo.script.gorm.portal.domain.pt.pds.PortletDataSource
import neo.script.gorm.portal.service.PortletDataSourceService
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@GraphQLMappingFlag(PortletDataSource)
class PortletDataSourceMapping extends GraphQLMapping {
    @Autowired
    PortletDataSourceService portletDataSourceService

    PortletDataSourceMapping() {
        description('Portlet数据源')
        property('dsName') { description('数据源名称') }
        property('type') { description('数据源类型，包括：LivebosQuery、RdbQuery') }
        query('getPortletData', String) {
            argument('dataSourceId', String)
            dataFetcher(new GetDataFetcher())
        }
    }


    class GetDataFetcher implements DataFetcher {
        @Override
        Object get(DataFetchingEnvironment environment) {
            return portletDataSourceService.getJsonData(environment.getArgument('dataSourceId'))
        }
    }
}
