package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
class PortletCalendar extends Portlet {

    static constraints = {
    }
    static graphql = true
}
