package neo.script.gorm.portal.domain.pt

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import neo.script.gorm.portal.util.Consts
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = PortalRow)
class PortalCol {
    String id

    String colName
    PortalRow row
    /**
     * colOrder   栅格顺序，flex 布局模式下有效	number	0
     * span 栅格占位格数，为 0 时相当于 display: none	number	-
     * 优先于：exColProps
     */
    Integer colOrder
    Integer span
    String style = Consts.STYLE_FLEX_ROW

    /**
     * @see https://ant.design/components/grid-cn/#Col
     */
    String exColProps = Consts.DEFAULT_COL_PROPS

    Date dateCreated
    Date lastUpdated

    static mapping = {
        row fetch: 'join', lazy: false
    }
    static constraints = {
        colName unique: true
        style maxSize: 1024
        exColProps maxSize: 512
    }
    static graphql = GraphQLMapping.build {
        description('门户展示列（一行包含多列）')
        property('colName') { description('列名') }
        property('row') { description('所属的展示行') }
        property('colOrder') { description('列序号') }
        property('span') { description('栅格占位格数（一行共24格），为 0 时相当于 display: none') }
        property('style') { description('自定义css') }
        property('exColProps') { description('其它属性，参考https://ant.design/components/grid-cn/#Col') }
    }

    static PortalCol DEMO_ROW1_COL1 = new PortalCol('DEMO_ROW1_COL1', PortalRow.DEMO_ROW1, 1, 8)
    static PortalCol DEMO_ROW1_COL2 = new PortalCol('DEMO_ROW1_COL2', PortalRow.DEMO_ROW1, 2, 8)
    static PortalCol DEMO_ROW1_COL3 = new PortalCol('DEMO_ROW1_COL3', PortalRow.DEMO_ROW1, 3, 8)
    static PortalCol DEMO_ROW2_COL1 = new PortalCol('DEMO_ROW2_COL1', PortalRow.DEMO_ROW2, 1, 6, Consts.STYLE_FLEX_COL)
    static PortalCol DEMO_ROW2_COL2 = new PortalCol('DEMO_ROW2_COL2', PortalRow.DEMO_ROW2, 2, 12, Consts.STYLE_FLEX_COL)
    static PortalCol DEMO_ROW2_COL3 = new PortalCol('DEMO_ROW2_COL3', PortalRow.DEMO_ROW2, 3, 6, Consts.STYLE_FLEX_COL)


    static initList = [DEMO_ROW1_COL1, DEMO_ROW1_COL2, DEMO_ROW1_COL3, DEMO_ROW2_COL1, DEMO_ROW2_COL2, DEMO_ROW2_COL3]
}
