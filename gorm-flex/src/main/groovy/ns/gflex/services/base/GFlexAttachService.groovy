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
        super.deleteByIds(idList, domain)
        attachmentService.deleteInfoByOwners(idList)
    }

    List queryAttachByOwner(def ownerId) {
        attachmentService.queryByOwner(ownerId)
    }

    /**
     *
     * @param fileName
     * @param data
     * @param ownerId
     * @param uploadId 回传前台的标识符，多个同时进行时可以区分
     * @return
     */
    def upload(String fileName, byte[] data, String ownerId, String uploadId) {
        def info = attachmentService.saveWithByte(fileName, ownerId, ownerName, data)
        //后台实际fileId可能和前台传入的不同，返回前台传入的fileId，告知本附件已完成上传
        return [info: info, uploadId: uploadId]
    }

    /**
     *
     * @param ownerId
     * @param fileId
     * @param downloadId 回传前台的标识符，多个同时进行时可以区分
     * @return
     */
    def download(String ownerId, String fileId, downloadId) {
        def infoAndFile = attachmentService.getInfoAndFile(ownerId, fileId)
        return [info: infoAndFile.info, file: infoAndFile.file, downloadId: downloadId]
    }

    def removeAttach(String ownerId, String fileId) {
        attachmentService.deleteInfoByOwnerAndFileId(ownerId, fileId);
    }

}
