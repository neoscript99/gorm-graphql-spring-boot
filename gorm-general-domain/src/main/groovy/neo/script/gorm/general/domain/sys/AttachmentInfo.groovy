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
    String fileId
    String ownerId
    String ownerName

    Date dateCreated

    static mapping = {
        ownerId index: 'idx_attach_owner'
        fileId index: 'idx_attach_file_id'
        dateCreated index: 'idx_attach_date'
    }

    static constraints = {
        fileId maxSize: 80
        ownerId maxSize: 80, nullable: true
        name maxSize: 256
    }
    static graphql = true
}
