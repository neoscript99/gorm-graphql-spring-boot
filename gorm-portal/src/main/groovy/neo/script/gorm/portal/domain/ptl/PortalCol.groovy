package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.util.Consts

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = PortalRow)
class PortalCol {
    String id

    String colName
    PortalRow row
    Integer colOrder    //栅格顺序，flex 布局模式下有效	number	0
    Integer span    //栅格占位格数，为 0 时相当于 display: none	number	-
    String style = Consts.STYLE_FLEX_ROW

    //以下非必输项
    Integer colOffset    //栅格左侧的间隔格数，间隔内不可以有栅格	number	0
    Integer pull    //栅格向左移动格数	number	0
    Integer push    //栅格向右移动格数	number	0
    /**
     * <Col xs={{ span: 5, offset: 1 }} lg={{ span: 6, offset: 2 }}>
     * 或只设置span
     * <Col xs={2} sm={4} md={6} lg={8} xl={10}>
     */
    String xs    //<576px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-
    String sm    //≥576px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-
    String md    //≥768px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-
    String lg    //≥992px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-
    String xl    //≥1200px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-
    String xxl    //≥1600px 响应式栅格，可为栅格数或一个包含其他属性的对象	number|object	-

    Date dateCreated
    Date lastUpdated

    static mapping = {
        row fetch: 'join', lazy: false
    }
    static constraints = {
        colName unique: true
        colOffset nullable: true
        pull nullable: true
        push nullable: true
        xs nullable: true
        sm nullable: true
        md nullable: true
        lg nullable: true
        xl nullable: true
        xxl nullable: true
        style maxSize: 256
    }
    static graphql = true
    static PortalCol DEMO_ROW1_COL1 = new PortalCol('DEMO_ROW1_COL1', PortalRow.DEMO_ROW1, 1, 24)
    static PortalCol DEMO_ROW2_COL1 = new PortalCol('DEMO_ROW2_COL1', PortalRow.DEMO_ROW2, 1, 6, Consts.STYLE_FLEX_COL)
    static PortalCol DEMO_ROW2_COL2 = new PortalCol('DEMO_ROW2_COL2', PortalRow.DEMO_ROW2, 2, 12)
    static PortalCol DEMO_ROW2_COL3 = new PortalCol('DEMO_ROW2_COL3', PortalRow.DEMO_ROW2, 3, 6, Consts.STYLE_FLEX_COL)


    static initList = [DEMO_ROW1_COL1, DEMO_ROW2_COL1, DEMO_ROW2_COL2, DEMO_ROW2_COL3]
}
