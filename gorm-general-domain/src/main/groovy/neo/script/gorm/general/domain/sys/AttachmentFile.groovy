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
@ToString(includes = 'id')
@EqualsAndHashCode(includes = 'id')
class AttachmentFile {
    String id
    byte[] data;

    static constraints = {
        data maxSize: 1024 * 1024 * 20
    }

    static graphql = true
}
