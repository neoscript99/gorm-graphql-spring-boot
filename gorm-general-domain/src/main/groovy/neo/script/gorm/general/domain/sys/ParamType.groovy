package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian

/**
 * 系统参数类型
 * @since Dec 23, 2010
 * @author wangchu
 */
@Entity
@TupleConstructor
@ToString(includePackage = false, includes = 'name')
@EqualsAndHashCode(includes = 'code')
@InitializeDomian
class ParamType {

    static final ParamType SYSTEM = (new ParamType(code: 'system', name: '系统参数', seq: 1))
    String code
    String name
    Integer seq

    static mapping = {
        id name: 'code', generator: 'assigned'
        sort 'seq'
    }
    static final initList = [SYSTEM]
}
