package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includes = 'name,fileSize')
@EqualsAndHashCode(includes = 'id')
class AttachmentInfo {
    String id
    String name
    Long fileSize
    String fileHash
    String fileId
    Date dateCreated

    static mapping = {
        dateCreated index: 'idx_attach_date'
        fileHash index: 'idx_attach_hash'
    }

    static constraints = {
        name maxSize: 256
        fileHash maxSize: 80
    }
    static graphql = true
}
