package neo.script.gorm.portal.domain.ptl.live

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
class LiveQuery {
    String id

    String objectName   //对象名,
    String condition    //ID>0 查询条件,
    //queryOption
    String valueOption = 'BOTH'//值输出(针对内部对象,多值对象,字典)DISPLAY|VALUE|BOTH
    Integer batchNo = 1 //1(当前页数),
    Integer batchSize = 50//50(每页记录数),
    Boolean queryCount = true//true(是否需要返回记录总数)}

    Date dateCreated
    Date lastUpdated

    static mapping = {
    }
    static constraints = {
    }
    static graphql = true
    static initList = []
}
