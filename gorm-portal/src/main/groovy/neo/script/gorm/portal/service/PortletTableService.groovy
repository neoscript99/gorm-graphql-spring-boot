package neo.script.gorm.portal.service

import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.ptl.PortalDbQuery
import neo.script.gorm.portal.domain.ptl.PortletTable
import neo.script.util.JsonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PortletTableService extends AbstractService<PortletTable> {
    @Autowired
    PortalDbService dbService

    List<String> getData(String id) {
        PortalDbQuery dbQuery = get(id).dbQuery
        def sql = dbService.getSql(dbQuery.db)
        return sql.rows(dbQuery.sql).collect { JsonUtil.toJson(it) }
    }
}
