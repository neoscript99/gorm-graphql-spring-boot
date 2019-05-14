package neo.script.gorm.portal.domain.ptl

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import neo.script.gorm.general.initializer.InitializeDomian

@Entity
@ToString(includePackage = false, includes = 'label')
@EqualsAndHashCode(includes = 'id')
@InitializeDomian(profiles = 'dev')
class SampleLink {
    String id
    String label
    String linkUrl
    String imageUrl
    Date dateCreated
    Date lastUpdated

    static constraints = {
        imageUrl nullable: true
    }

    static initList = [
            new SampleLink(label: '金蝶OA系统EAS', linkUrl: 'http://www.bing.com'),
            new SampleLink(label: '新绩效考核系统', linkUrl: 'http://www.bing.com'),
            new SampleLink(label: '信贷管理系统', linkUrl: 'http://www.bing.com')
    ]
}
