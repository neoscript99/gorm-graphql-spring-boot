package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
class PortletCalendar extends Portlet {
    String titleField
    String dateLink
    String beginTimeField
    String endTimeField
    String timeFormat = 'YYYY-MM-DD HH:mm:ss'
    static constraints = {
        dateLink maxSize: 256
        endTimeField nullable: true
    }
    static graphql = true
}
