package neo.script.gorm.general.service

import neo.script.gorm.general.config.data.MenuInitializer
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.repositories.GeneralRepository
import spock.lang.Specification

class MenuServiceSpec extends Specification {

    def 'menu tree test'() {
        given:
        GeneralRepository generalRepository = Mock(GeneralRepository)
        def list = []
        generalRepository.countAll(_)
        generalRepository.saveEntity(_) >> { Menu menu ->
            menu.id = UUID.randomUUID().toString()
            println menu
            list << menu
            return menu
        }
        MenuInitializer menuInitializer = new MenuInitializer(generalRepository: generalRepository)

        when:
        menuInitializer.init()

        then:
        list.size() > 5
    }
}
