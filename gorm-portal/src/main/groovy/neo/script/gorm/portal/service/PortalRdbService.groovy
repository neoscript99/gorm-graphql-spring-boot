package neo.script.gorm.portal.service

import groovy.sql.Sql
import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.pt.pds.RdbServer
import org.springframework.stereotype.Service

import java.sql.SQLException

@Service
class PortalRdbService extends AbstractService<RdbServer> {
    private final groovySqlMap = new HashMap<String, Sql>()

    synchronized Sql getSql(RdbServer db) {
        def sql = groovySqlMap.get(db.id)
        if (sql) {
            try {
                sql.execute(db.testSql)
            }
            //连接被关闭后重新创建
            catch (SQLException e) {
                log.error('连接失效', e)
                sql = newSql(db)
            }
        } else {
            log.info('Create GroovySqL instance for {}', db.toString())
            sql = newSql(db)
        }
        return sql
    }

    Sql newSql(RdbServer db) {
        def sql = Sql.newInstance(db.url, db.username, db.password, db.driverClassName)
        groovySqlMap.put(db.id, sql)
        return sql
    }
}
