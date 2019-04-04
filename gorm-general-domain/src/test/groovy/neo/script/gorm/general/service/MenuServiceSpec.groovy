package neo.script.gorm.general.service

import neo.script.gorm.general.config.data.MenuInitializer
import neo.script.gorm.general.domain.sys.Menu
import neo.script.gorm.general.repositories.GeneralRepository
import org.grails.datastore.gorm.GormEntity
import spock.lang.Shared
import spock.lang.Specification

class MenuServiceSpec extends Specification {
    @Shared
    GeneralRepository generalRepository = Mock(GeneralRepository)
    @Shared
    def menuList = []

    def setupSpec() {
        generalRepository.countAll(_) >> 0
        generalRepository.saveEntity(_) >> { GormEntity entity ->
            entity.id = UUID.randomUUID().toString()
            if (Menu.isAssignableFrom(entity.class))
                menuList << entity
            return entity
        }
        generalRepository.get(_, _) >> { Class cls, String id ->
            return menuList.find { it.id == id }
        }

        MenuInitializer menuInitializer = new MenuInitializer(generalRepository: generalRepository)
        menuInitializer.init()
    }

    def 'menu tree test'() {
        given:

        def menuService = new MenuService(generalRepository: generalRepository)
        def tree = menuService.generateTree(menuList)
        println(tree)

        expect:
        tree.subMenus.size() == 4
    }
}
