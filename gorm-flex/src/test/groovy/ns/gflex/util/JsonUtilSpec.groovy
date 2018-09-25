package ns.gflex.util

import spock.lang.Specification

/**
 * Created by Neo on 2017-09-13.
 */
class JsonUtilSpec extends Specification {

    def 'test bean list to map list'() {
        given:
        Department department = new Department(name: '总部')
        List userList = [new User(name: '用户1', dept: department), new User(name: '用户2', dept: department)]

        when:
        List list = JsonUtil.beanListToMapList(userList)
        println list

        then:
        list.size() == 2
    }

    def 'test bean to map'() {
        given:
        Department department = new Department(name: '总部')
        User user = new User(name: '用户', dept: department)

        when:
        Map map = JsonUtil.beanToMap(user)

        then:
        map.dept.name == '总部'
    }

    def 'test map to bean'() {
        given:
        def map = [name: 'aaa', dept: [name: 'depta']]

        when:
        def user = JsonUtil.mapToBean(map, User)

        then:
        user.dept.name == 'depta'
    }

    static class Department {
        String name
    }

    static class User {
        String name
        Department dept
    }
}
