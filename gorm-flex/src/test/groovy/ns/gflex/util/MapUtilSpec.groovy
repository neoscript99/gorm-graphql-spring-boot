package ns.gflex.util

import spock.lang.Specification

/**
 * Created by Neo on 2017-09-15.
 */
class MapUtilSpec extends Specification {
    def 'get by nest key'() {
        given:
        Map map = [name: 'a', dept: [name: 'd']]

        when:
        def name = MapUtil.getByNestKey(map, 'name')
        def result = MapUtil.getByNestKey(map, 'dept.name')

        then:
        name == 'a'
        result == 'd'
    }
}
