package ns.gflex.services.base

import neo.script.gorm.general.domain.sys.AttachmentFile
import neo.script.gorm.general.domain.sys.AttachmentInfo
import neo.script.gorm.general.service.AttachmentService
import org.springframework.beans.factory.annotation.Autowired


/**
 * 附件服务类
 * @date 2013-7-17
 * @author 王楚
 *
 */
abstract class GFlexAttachService extends GFlexLabelService {

    static public String ATTACH_INFO_FIELD = 'gflexAttachInfo';
    static public String ATTACH_TEMP_ID_PREFIX = 'gflexTempAttach_';

    @Autowired
    AttachmentService attachmentService;

    def save(Map map, boolean isMerge = false, Object domain = null) {
        log.info("GFlexAttachService.save")
        log.debug "info:\n     $map"
        def entity = super.save(map, isMerge, domain);
        if (!map.id && map[ATTACH_INFO_FIELD]?.ownerId)
            updateAttachOwner(map[ATTACH_INFO_FIELD].ownerId, entity.id)

        return entity;
    }

    void deleteByIds(List idList, Object domain = null) {
        removeAttachByOwners(idList)
        super.deleteByIds(idList, domain)
    }

    List queryAttachByOwner(def ownerId) {
        list([eq: [['ownerId', withPrefix(ownerId)]]], AttachmentInfo);
    }

    /**
     * @param fileName 文件名
     * @param data 文件原始数据
     * @return fileId 标志上传完毕
     */
    def upload(String fileName, byte[] data, String fileId, def ownerId) {
        attachmentService.saveWithByte(fileName, withPrefix(ownerId), data, fileId)
        return fileId
    }

    def download(String fileId) {
        log.info "download file $fileId"
        def af = AttachmentFile.findByFileId(fileId)
        def ai = AttachmentInfo.findByFileId(fileId)
        [data: af.data, name: ai.name, fileId: af.fileId]
    }

    def removeAttach(String fileId) {
        log.info "remove file $fileId"
        removeAttachByIds([fileId])
    }

    def removeAttachByIds(List ids) {
        log.info "removeByIds $ids"
        if (ids) {
            generalRepository.deleteMatch(AttachmentInfo, ['in': [['fileId', ids]]])
            generalRepository.deleteMatch(AttachmentFile, ['in': [['fileId', ids]]])
        }
    }

    def removeAttachByOwner(def ownerId) {
        log.info "removeByOwner $ownerId"
        removeAttachByIds(queryAttachByOwner(ownerId)*.fileId)
    }

    def removeAttachByOwners(List owners) {
        log.info "removeByOwners $owners"
        def ownersList = []
        owners.each {
            ownersList << (withPrefix(it))
        }
        removeAttachByIds(list(['in': [['ownerId', ownersList]]], AttachmentInfo)*.fileId)
    }

    void updateAttachOwner(String oldId, def newId) {
        generalRepository.updateMatch(AttachmentInfo,
                [eq: [['ownerId', withPrefix(oldId)]]],
                ['ownerId': withPrefix(newId)])

        //删除一些没有所属对象的附件
        removeAttachByIds(list([like: [['ownerId', "${withPrefix(ATTACH_TEMP_ID_PREFIX)}%".toString()]],
                                lt  : [['dateCreated', new Date() - 1]]],
                AttachmentInfo)*.fileId)
    }
}
