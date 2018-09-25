package ns.gflex.config.data

import ns.gflex.config.initialize.InitializeDomian
import ns.gflex.domain.Department
import spock.lang.Specification

import java.lang.reflect.Field

/**
 * Created by Neo on 2017-09-22.
 */
class InitializeDomianSpec extends Specification {
    def 'get value'() {
        given:
        InitializeDomian initializeDomian = Department.getAnnotation(InitializeDomian)
        Field initField = Department.getDeclaredField(initializeDomian.value())
        when:
        initField.setAccessible(true)
        println initField.get(Department)

        then:
        true
    }
}
