package ns.gflex.services.base

import neo.script.gorm.general.domain.sys.AttachmentInfo

/**
 * 支持主类和附件类建立1对1关系
 * AttachmentInfo的ownerId 和 主类的fileInfo
 * @date 2018-10-1
 * @author 王楚
 *
 */
abstract class GFlex1To1AttachService extends GFlexAttachService {

    def save(Map map, boolean isMerge = false, Object domain = null) {
        //仅针对包含Uploader控件的做设置
        if(map[ATTACH_INFO_FIELD]) {
            def infoIds = map[ATTACH_INFO_FIELD].infoIds
            map.fileInfo = infoIds ? [id: infoIds[0]] : null;
        }
        super.save(map, isMerge, domain);
    }

    def upload(String fileName, byte[] data, String ownerId, String uploadId) {
        def result = super.upload(fileName, data, ownerId, uploadId)
        if (!ownerId.startsWith(ATTACH_TEMP_ID_PREFIX))
            makeRelation(ownerId, result.info)
        return result;
    }

    /**
     * 检查ownerId是否存在，如果存在建立关联，tempId不用执行本步骤
     * @param ownerId
     * @param info
     */
    void makeRelation(String ownerId, AttachmentInfo info) {
        generalRepository.get(domainClass, ownerId).fileInfo = info
    }

    def removeAttach(String ownerId, String fileId) {
        if (!ownerId.startsWith(ATTACH_TEMP_ID_PREFIX))
            deleteRelation(ownerId, fileId)
        super.removeAttach(ownerId, fileId);
    }

    void deleteRelation(String ownerId, String fileId) {
        generalRepository.get(domainClass, ownerId).fileInfo = null
    }
}
