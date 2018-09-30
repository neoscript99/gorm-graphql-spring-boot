package ns.gflex.services.base

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

    /**
     * 上传附件时为临时ID，owner保存后需进行更新
     * @param oldId
     * @param newId
     */
    void updateAttachOwner(String oldId, def newId) {
        attachmentService.updateMatch([eq: [['ownerId', oldId]]], ['ownerId': newId])
    }

    @Override
    void deleteByIds(List idList, Object domain = null) {
        attachmentService.deleteInfoByOwners(idList)
        super.deleteByIds(idList, domain)
    }

    List queryAttachByOwner(def ownerId) {
        attachmentService.queryByOwner(ownerId)
    }

    /**
     * @param fileName 文件名
     * @param data 文件原始数据
     * @return fileId 标志上传完毕
     */
    def upload(String fileName, byte[] data, String ownerId, String uploadId) {
        def info = attachmentService.saveWithByte(fileName, ownerId, ownerName, data)
        //后台实际fileId可能和前台传入的不同，返回前台传入的fileId，告知本附件已完成上传
        return [fileId: info.fileId, uploadId: uploadId]
    }

    def download(String ownerId, String fileId) {
        return attachmentService.getInfoAndFile(ownerId, fileId)
    }

    def removeAttach(String ownerId, String fileId) {
        attachmentService.deleteInfoByOwnerAndFileId(ownerId, fileId);
    }

    def removeAttachByOwner(def ownerId) {
        attachmentService.deleteInfoByOwners([ownerId])
    }
}
