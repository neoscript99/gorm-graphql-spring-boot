package ns.gflex.domain

import neo.script.gorm.general.domain.sys.*
import neo.script.gorm.general.repositories.GeneralRepository
import neo.script.util.EncoderUtil
import neo.script.util.JsonUtil
import ns.gflex.GormFlexApp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by Neo on 2017-09-29.
 */
@SpringBootTest(classes = GormFlexApp.class)
@Transactional
@Commit
@Stepwise
class GormSpec extends Specification {
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    ApplicationArguments applicationArguments


    static Map savedMap

    def 'new role'() {
        given:
        Role role = generalRepository.saveEntity(Role.PUBLIC)
        savedMap = JsonUtil.beanToMap(role)
        println role.dump()
        expect:
        role.lastUpdated == role.dateCreated
    }

    def 'check id'() {
        given:
        println(savedMap)
        println(savedMap.dateCreated)
        expect:
        savedMap.id != null
    }

    def 'update lastUpdated'() {
        given:
        Role resultRole = generalRepository.saveMap(Role, savedMap)

        expect: '插入数据库时更新lastUpdated，entity中不会改变'
        resultRole.lastUpdated == resultRole.dateCreated
    }

    def 'check autoTimestamp'() {
        given:
        Role resultRole = generalRepository.get(Role, savedMap.id)

        expect:
        resultRole.lastUpdated > resultRole.dateCreated
    }

    def 'find by example'() {
        given:
        def ex = new Role(roleName: savedMap.roleName, editable: null, enabled: null)
        def roleList = generalRepository.findByExample(ex)
        def userList = generalRepository.findByExample(User.ADMIN)
        expect:
        roleList.size() > 0
        !userList
    }

    def 'update and delete by match'() {
        given:
        def dept = new Department(name: '测试', seq: 1)
        generalRepository.saveEntity(dept)
        generalRepository.saveEntity((new User(account: 'test1', name: '测试1', dept: dept,
                editable: false, password: EncoderUtil.sha256('admin'))))
        generalRepository.saveEntity((new User(account: 'test2', name: '测试2', dept: dept,
                editable: false, password: EncoderUtil.sha256('admin'))))
        expect:
        generalRepository.updateMatch(User, [dept: [eq: [['id', dept.id]]]], [editable: true]) == 2
        generalRepository.updateMatch(User, null, [editable: true]) == 2
        generalRepository.deleteMatch(User, [dept: [eq: [['id', dept.id]]]]) == 2
        generalRepository.deleteMatch(User, null) == 0
        generalRepository.deleteMatch(User, [dept: [eq: [['id', -1]]]]) == 0
    }
}
