package neo.script.gorm.portal.config.pds

import neo.script.gorm.general.domain.sys.Token
import neo.script.gorm.general.util.TokenHolder
import spock.lang.Specification

class UserDateFormatterSpec extends Specification {
    def 'f test'() {
        given:
        TokenHolder.token = new Token([username: 'admin'])

        UserDateFormatter udf = new UserDateFormatter();
        def query = '''select * from emp where emp_id='${username}' and last_updated='${yesterday}' #${today} ${last_month} ${last_year}'''
        def res = udf.format(query)
        println(res)
        expect:
        res.contains('admin')
    }
}
