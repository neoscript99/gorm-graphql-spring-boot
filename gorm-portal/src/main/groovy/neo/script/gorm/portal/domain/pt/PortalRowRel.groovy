package neo.script.gorm.portal.domain.pt

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

@Entity
@TupleConstructor(excludes = 'id, dateCreated, lastUpdated, version')
@ToString(includePackage = false, includes = 'id, lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev', depends = [Portal, PortalRow])
class PortalRowRel {
    String id
    Portal portal
    PortalRow row
    Integer rowOrder
    Date dateCreated
    Date lastUpdated

    static mapping = {
        portal fetch: 'join', lazy: false
        row fetch: 'join', lazy: false
    }
    static constraints = {
    }
    static graphql = GraphQLMapping.build {
        description('门户和展示行关联信息（多对多）')
        property('portal') { description('门户') }
        property('row') { description('展示行') }
        property('rowOrder') { description('行序号') }
    }
    static initList = [
            new PortalRowRel(Portal.PERSONAL_PORTAL, PortalRow.DEMO_ROW1, 1),
            new PortalRowRel(Portal.PERSONAL_PORTAL, PortalRow.DEMO_ROW2, 2)
    ]
}
