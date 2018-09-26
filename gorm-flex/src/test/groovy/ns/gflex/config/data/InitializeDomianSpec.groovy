package ns.gflex.config.data

import neo.script.gorm.general.domain.sys.Department
import neo.script.gorm.general.initializer.InitializeDomian
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
