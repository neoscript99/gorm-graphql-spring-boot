package neo.script.gorm.general.domain.sys

import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 把附件文件信息和属性分开，附件最大20M
 * @author neo*
 */
@Entity
@ToString(includes = 'fileId,refCount')
@EqualsAndHashCode(includes = 'fileId')
class AttachmentFile {
    String fileId
    Integer refCount = 0 //引用计数
    byte[] data;
    static mapping = {
        id name: 'fileId', generator: 'assigned'
        /**
         * todo 默认类型db2报错（varchar(20170000) is not valid ），配置blob无法转换，后续再优化
         */
        data type: 'blob'
    }

    static constraints = {
        fileId maxSize: 80
        data maxSize: 1024 * 1024 * 20
    }
}
