package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Entity
@ToString(includes = 'name')
@EqualsAndHashCode(includes = 'fileId')
class AttachmentInfo {
    String fileId
    String name
    Long fileSize
    Date dateCreated
    String ownerId
    static mapping = {
        id name: 'fileId', generator: 'assigned'
        ownerId index: 'idx_attach_owner'
        dateCreated index: 'idx_attach_date'
    }

    static constraints = {
        fileId maxSize: 64
        ownerId maxSize: 128
        name maxSize: 256
    }

    @Override
    public String toString() {
        return "Attachment - $name";
    }
    static graphql = true
}
