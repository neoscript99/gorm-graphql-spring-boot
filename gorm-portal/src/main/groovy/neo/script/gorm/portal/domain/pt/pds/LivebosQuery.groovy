package neo.script.gorm.portal.domain.pt.pds

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = LivebosServer)
class LivebosQuery extends PortletDataSource {

    LivebosServer livebosServer
    String objectName   //对象名,
    /**
     * 查询对象的参数，键值对如：{ "PUid":0 }*/
    String params
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
        params nullable: true, maxSize: 128
        condition blank: true, maxSize: 256
    }
    static graphql = GraphQLMapping.build {
        description('LiveBOS远程服务')
        property('livebosServer') { description('所属的LiveBOS服务端') }
        property('objectName') { description('对象名') }
        property('params') { description('传入的参数集合(当要查询的对象为查询对象,带参数的视图等要传入参数,实体对象则传入空值)') }
        property('condition') { description('查询的附加条件(查询对象无效),如： “ID=1000” 等') }
        property('valueOption') { description('值输出，显示值：DISPLAY 存储值：VALUE') }
        property('batchNo') { description('当前页数') }
        property('batchSize') { description('每页记录数') }
        property('queryCount') { description('是否需要返回记录总数') }
    }
    //通讯录，查询1000
    static LivebosQuery USER_LINK = new LivebosQuery('通讯录', 'LivebosQuery', LivebosServer.DEMO_SERVER,
            'tUserLink', null, '', 'DISPLAY', 1, 1000)
    static initList = [USER_LINK]
}
