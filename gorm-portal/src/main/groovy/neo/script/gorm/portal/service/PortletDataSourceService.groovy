package neo.script.gorm.portal.service

import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.config.pds.QueryFormatter
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.PortletDataSource
import neo.script.gorm.portal.domain.pt.pds.RdbQuery
import neo.script.util.JsonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class PortletDataSourceService extends AbstractService<PortletDataSource> {
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    LivebosServerService livebosServerService
    @Autowired
    PortalRdbService rdbService

    String getJsonData(String dataSourceId) {
        PortletDataSource ds = get(dataSourceId)
        if (ds.type == 'RdbQuery')
            return getRdbData(dataSourceId)
        if (ds.type == 'LivebosQuery')
            return getLivebosData(dataSourceId)
        return null
    }

    String getRdbData(String dataSourceId) {
        RdbQuery dbQuery = generalRepository.get(RdbQuery, dataSourceId)
        def sql = rdbService.getSql(dbQuery.db)
        JsonUtil.toJson(sql.rows(formatQuery(dbQuery.sql)))
    }

    String getLivebosData(String dataSourceId) {
        LivebosQuery query = generalRepository.get(LivebosQuery, dataSourceId)
        return livebosServerService.objectQuery(query, formatQuery(query.condition))
    }

    String formatQuery(String query) {
        def res = query
        applicationContext.getBeansOfType(QueryFormatter).values().sort().each {
            res = it.format(res)
        }
        return res
    }
}
