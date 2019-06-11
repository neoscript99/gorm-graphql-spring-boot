package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = PortalRow)
class PortalCol {
    String id

    PortalRow row
    Integer colOrder    //դ��˳��flex ����ģʽ����Ч	number	0
    Integer span    //դ��ռλ������Ϊ 0 ʱ�൱�� display: none	number	-

    //���·Ǳ�����
    Integer colOffset    //դ�����ļ������������ڲ�������դ��	number	0
    Integer pull    //դ�������ƶ�����	number	0
    Integer push    //դ�������ƶ�����	number	0
    /**
     * <Col xs={{ span: 5, offset: 1 }} lg={{ span: 6, offset: 2 }}>
     * ��ֻ����span
     * <Col xs={2} sm={4} md={6} lg={8} xl={10}>
     */
    String xs    //<576px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String sm    //��576px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String md    //��768px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String lg    //��992px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String xl    //��1200px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String xxl    //��1600px ��Ӧʽդ�񣬿�Ϊդ������һ�������������ԵĶ���	number|object	-
    String style = '''{ "display": "flex", "justifyContent": "center", "flexWrap": "wrap" }'''

    Date dateCreated
    Date lastUpdated

    static mapping = {
        row fetch: 'join', lazy: false
    }
    static constraints = {
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
    static PortalCol DEMO_ROW1_COL1 = new PortalCol(PortalRow.DEMO_ROW1, 1, 24)
    static PortalCol DEMO_ROW2_COL1 = new PortalCol(PortalRow.DEMO_ROW2, 1, 6)
    static PortalCol DEMO_ROW2_COL2 = new PortalCol(PortalRow.DEMO_ROW2, 2, 12)
    static PortalCol DEMO_ROW2_COL3 = new PortalCol(PortalRow.DEMO_ROW2, 3, 6)
    static initList = [DEMO_ROW1_COL1, DEMO_ROW2_COL1, DEMO_ROW2_COL2, DEMO_ROW2_COL3]
}
