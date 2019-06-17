package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
/**
 * 定制的Portlet，通过type来做对应实现
 */
class PortletCustomize extends Portlet {
    static graphql = true
}
