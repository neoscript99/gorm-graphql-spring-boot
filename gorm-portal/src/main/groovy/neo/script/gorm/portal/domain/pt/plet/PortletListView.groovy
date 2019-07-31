package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.domain.pt.pds.LivebosQuery
import neo.script.gorm.portal.domain.pt.pds.LivebosServer
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = LivebosQuery)
class PortletListView extends Portlet {
    String titleTemplate
    String cateField
    String dateField
    String extraLink
    String titleLink
    String iconField
    //nowrap 单行显示， normal 自动换行
    String titleWhiteSpace = 'nowrap'
    //标题最大显示字数，titleWhiteSpace配置为nowrap才生效
    Integer titleMaxSize
    String rowKey = 'ID'
    String fromDateFormat = 'YYYY-MM-DD HH:mm:ss'
    String toDateFormat = 'YYYY-MM-DD'

    static constraints = {
        cateField nullable: true
        iconField nullable: true
        dateField nullable: true
        extraLink nullable: true, maxSize: 512
        titleLink nullable: true, maxSize: 512
        fromDateFormat nullable: true
        toDateFormat nullable: true
        titleMaxSize nullable: true
    }
    static graphql = GraphQLMapping.build {
        description('列表组件')
        property('titleTemplate') { description('标题模板') }
        property('cateField') { description('分类字段') }
        property('dateField') { description('日期字段') }
        property('extraLink') { description('更多链接地址') }
        property('titleLink') { description('标题链接地址') }
        property('iconField') { description('图标字段') }
        property('rowKey') { description('ID字段') }
        property('fromDateFormat') { description('日期原格式') }
        property('toDateFormat') { description('日期转换格式') }
        property('titleWhiteSpace') { description('标题whiteSpace的CSS属性') }
    }

    static USER_LINK_LIST = new PortletListView('通讯录', 'PortletListView', LivebosQuery.USER_LINK,
            null, '{Name}({UserID})', 'Grade', 'ChgPwdTime',
            "$LivebosServer.DEMO_SERVER.serverRoot/UIProcessor?Table=tUserLink",
            "$LivebosServer.DEMO_SERVER.serverRoot/UIProcessor?Table=tUserLink")
    static initList = [USER_LINK_LIST]
}
