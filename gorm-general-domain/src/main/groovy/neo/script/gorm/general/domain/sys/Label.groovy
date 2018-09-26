package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includes = 'label')
@EqualsAndHashCode(includes = 'id')
class Label {
    String id
    String ownerId
    String label

    static mapping = {
        ownerId index: "idx_label_owner"
    }

    static constraints = {
        ownerId(unique: 'label', maxSize: 80)
        label empty: false
    }
    static graphql = true
}
