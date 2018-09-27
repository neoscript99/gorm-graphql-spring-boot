package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 把附件文件信息和属性分开，附件最大20M
 * @author neo
 *
 */
@Entity
@ToString(includes = 'fileId')
@EqualsAndHashCode(includes = 'fileId')
class AttachmentFile {
    String fileId
    String fileHash
    Integer refCount = 0 //引用计数
    byte[] data;
    static mapping = {
        id name: 'fileId', generator: 'assigned'
        fileHash index: 'idx_attach_hash'
    }

    static constraints = {
        fileId maxSize: 80
        data maxSize: 1024 * 1024 * 20
        fileHash maxSize: 80
    }
}
