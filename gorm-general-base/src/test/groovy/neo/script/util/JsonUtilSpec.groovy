package neo.script.util

import spock.lang.Specification


class JsonUtilSpec extends Specification {
    def "empty String to Json"() {
        given:
        println JsonUtil.fromJson('null', Map, false)
        println JsonUtil.fromJson('{}', Map, false)
        expect:
        true
    }
}
