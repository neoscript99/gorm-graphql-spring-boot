package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
class PortletTable extends Portlet {

    String columns
    String rowKey

    static constraints = {
        columns maxSize: 2048
    }
    static graphql = true

}
