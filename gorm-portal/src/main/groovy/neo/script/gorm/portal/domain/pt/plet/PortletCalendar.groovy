package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
class PortletCalendar extends Portlet {
    String titleField
    String beginTimeField
    String endTimeField
    String timeFormat = 'YYYY-MM-DD HH:mm:ss'
    static constraints = {
        endTimeField nullable: true
    }
    static graphql = true
}
