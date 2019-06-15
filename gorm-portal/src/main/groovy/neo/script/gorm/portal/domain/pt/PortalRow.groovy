package neo.script.gorm.portal.domain.pt

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class PortalRow {
    String id
    String rowName
    Integer gutter = 16   //栅格间隔，可以写成像素值或支持响应式的对象写法 { xs: 8, sm: 16, md: 24}	number/object	0
    String align    //flex 布局下的垂直对齐方式：top middle bottom	string	top
    String justify    //flex 布局下的水平排列方式：start end center space-around space-between	string	start
    String type    //布局模式，可选 flex，现代浏览器 下有效	string

    Date dateCreated
    Date lastUpdated

    static hasMany = [cols: PortalCol]
    static mapping = {
        cols cascade: 'all-delete-orphan', fetch: 'join', lazy: false
    }
    static constraints = {
        rowName unique: true
        align nullable: true
        gutter nullable: true
        justify nullable: true
        type nullable: true
    }
    static graphql = true
    static DEMO_ROW1 = new PortalRow('DEMO_ROW1')
    static DEMO_ROW2 = new PortalRow('DEMO_ROW2')
    static initList = [DEMO_ROW1, DEMO_ROW2]
}
