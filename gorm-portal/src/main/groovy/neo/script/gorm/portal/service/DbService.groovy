package neo.script.gorm.portal.service

import groovy.sql.Sql
import neo.script.gorm.general.service.AbstractService
import neo.script.gorm.portal.domain.ptl.DBInfo
import org.springframework.stereotype.Service

@Service
class DbService extends AbstractService<DBInfo> {
    private final groovySqlMap = new HashMap<String, Sql>()

    synchronized Sql getSql(DBInfo dbInfo) {
        def sql = groovySqlMap.get(dbInfo.id)
        if (sql) {
            try {
                sql.execute(dbInfo.testSql)
            }
            //连接被关闭后重新创建
            catch (SQLException) {
                sql = newSql(dbInfo)
            }
        } else {
            log.info('Create GroovySqL instance for {}', dbInfo.toString())
            sql = newSql(dbInfo)
            groovySqlMap.put(dbInfo.id, sql)
        }
        return sql
    }

    Sql newSql(DBInfo dbInfo) {
        return Sql.newInstance(dbInfo.url, dbInfo.username, dbInfo.password, dbInfo.driverClassName)
    }
}
