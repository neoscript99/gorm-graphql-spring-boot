package ns.gflex.config.data

import neo.script.gorm.general.config.data.MenuInitializer
import neo.script.gorm.general.repositories.GeneralRepository
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by Neo on 2017-08-25.
 */
class DataInitializerSpec extends Specification {
    @Shared
    GeneralRepository persistenceService

    def setupSpec() {
        persistenceService = Mock(GeneralRepository)
        persistenceService.saveEntity(_) >> {
            it[0]
        }
        persistenceService.count(_, _) >> 0
    }

    def "GFlexData init test"() {
        given:
        MenuInitializer menuInitializer = new MenuInitializer()
        menuInitializer.generalRepository = persistenceService

        when:
        menuInitializer.init()

        //被自定义闭包接管，mock方法未调用
        then:
        0 * persistenceService.saveEntity(_)
    }
}
