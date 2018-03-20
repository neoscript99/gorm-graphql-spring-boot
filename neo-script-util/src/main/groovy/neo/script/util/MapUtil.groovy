package neo.script.util

/**
 * Created by Neo on 2017-09-15.
 */
class MapUtil {
    static def getByNestKey(Map map, String key) {
        def result = map
        key.split('\\.').each {
            result = result.get(it)
        }
        return result
    }
}
