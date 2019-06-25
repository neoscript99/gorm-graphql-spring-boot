package neo.script.gorm.portal.domain.pt.plet

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

@Entity
@TupleConstructor(includeSuperProperties = true, excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
class PortletListView extends Portlet {
    String titleFields
    String cateField
    String dateField
    String extraLink
    String titleLink
    String iconField
    String rowKey = 'ID'
    String fromDateFormat = 'YYYY-MM-DD HH:mm:ss'
    String toDateFormat = 'YYYY-MM-DD'

    static constraints = {
        cateField nullable: true
        iconField nullable: true
        dateField nullable: true
        extraLink nullable: true, maxSize: 512
        titleLink nullable: true, maxSize: 512
        fromDateFormat nullable: true
        toDateFormat nullable: true
    }
    static graphql = true
}
