package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
class PortletCalendar extends Portlet {
    String titleField
    String titleLink
    String dateLink
    String beginTimeField
    String endTimeField
    String timeFormat = 'YYYY-MM-DD HH:mm:ss'
    static constraints = {
        titleLink maxSize: 256
        dateLink maxSize: 256
        endTimeField nullable: true
    }
    static graphql = GraphQLMapping.build {
        description('日历组件')
        property('titleField') { description('日程描述信息') }
        property('titleLink') { description('日程描述链接') }
        property('dateLink') { description('日程链接地址') }
        property('beginTimeField') { description('开始时间字段名') }
        property('endTimeField') { description('结束时间字段名') }
        property('timeFormat') { description('时间字符串格式') }
    }
}
