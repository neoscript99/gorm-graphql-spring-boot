package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = LivebosServer)
class LivebosQuery extends PortletDataSource {

    LivebosServer livebosServer
    String objectName   //对象名,
    String condition = ''   //ID>0 查询条件,
    /**queryOption start**/
    //值输出(针对内部对象,多值对象,字典)DISPLAY|VALUE|BOTH
    //如果传入BOTH，每个字段会返回显示值和实际值两个值
    String valueOption = 'DISPLAY'
    Integer batchNo = 1 //1(当前页数),
    Integer batchSize = 10//(每页记录数),
    Boolean queryCount = true//true(是否需要返回记录总数)}
    /**queryOption end**/


    static mapping = {
        livebosServer fetch: 'join', lazy: false
    }
    static constraints = {
        condition blank: true
    }
    static graphql = true
    //通讯录，查询1000
    static LivebosQuery USER_LINK = new LivebosQuery('通讯录', 'LivebosQuery', LivebosServer.DEMO_SERVER,
            'tUserLink', '', 'DISPLAY', 1, 1000)
    static initList = [USER_LINK]
}
