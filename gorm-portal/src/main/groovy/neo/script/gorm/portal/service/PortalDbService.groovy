package neo.script.gorm.portal.service

import groovy.sql.Sql
import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.ptl.PortalDb
import org.springframework.stereotype.Service

@Service
class PortalDbService extends AbstractService<PortalDb> {
    private final groovySqlMap = new HashMap<String, Sql>()

    synchronized Sql getSql(PortalDb db) {
        def sql = groovySqlMap.get(db.id)
        if (sql) {
            try {
                sql.execute(db.testSql)
            }
            //连接被关闭后重新创建
            catch (SQLException) {
                sql = newSql(db)
            }
        } else {
            log.info('Create GroovySqL instance for {}', db.toString())
            sql = newSql(db)
            groovySqlMap.put(db.id, sql)
        }
        return sql
    }

    Sql newSql(PortalDb db) {
        return Sql.newInstance(db.url, db.username, db.password, db.driverClassName)
    }
}
