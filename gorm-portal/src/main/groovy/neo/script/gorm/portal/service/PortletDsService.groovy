package neo.script.gorm.portal.service

import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.pt.ds.PortletDs
import neo.script.gorm.portal.domain.pt.ds.RdbQuery
import neo.script.util.JsonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PortletDsService extends AbstractService<PortletDs> {
    @Autowired
    PortalRdbService rdbService

    List<String> getData(String id) {
        PortletDs ds = get(id)
        if (ds.type == 'RdbQuery')
            return getRdbData(id)
        return null
    }

    List<String> getRdbData(String id) {
        RdbQuery dbQuery = generalRepository.get(RdbQuery, id)
        def sql = rdbService.getSql(dbQuery.db)
        return sql.rows(dbQuery.sql).collect { JsonUtil.toJson(it) }
    }
}
