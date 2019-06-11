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
@InitializeDomian(profiles = 'dev')
class PortalRow {
    String id

    Integer gutter    //դ����������д������ֵ��֧����Ӧʽ�Ķ���д�� { xs: 8, sm: 16, md: 24}	number/object	0
    String align    //flex �����µĴ�ֱ���뷽ʽ��top middle bottom	string	top
    String justify    //flex �����µ�ˮƽ���з�ʽ��start end center space-around space-between	string	start
    String type    //����ģʽ����ѡ flex���ִ������ ����Ч	string

    Date dateCreated
    Date lastUpdated

    static hasMany = [cols: PortalCol]
    static mapping = {
        cols cascade: 'all-delete-orphan', fetch: 'join', lazy: false
    }
    static constraints = {
        align nullable: true
        gutter nullable: true
        justify nullable: true
        type nullable: true
    }
    static graphql = true
    static DEMO_ROW1 = new PortalRow(16)
    static DEMO_ROW2 = new PortalRow(16)
    static initList = [DEMO_ROW1, DEMO_ROW2]
}
