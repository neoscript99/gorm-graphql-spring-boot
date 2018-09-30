package neo.script.gorm.general.service

import grails.gorm.transactions.ReadOnly
import neo.script.gorm.general.domain.sys.AttachmentFile
import neo.script.gorm.general.domain.sys.AttachmentInfo
import neo.script.util.EncoderUtil
import org.springframework.stereotype.Service

@Service
class AttachmentService extends AbstractService<AttachmentInfo> {

    AttachmentInfo saveWithFile(File file, String ownerId, String ownerName) {
        if (!file.isFile())
            throw new RuntimeException("$file.path,该文件不存在")

        return saveWithByte(file.name, ownerId, ownerName, file.getBytes());
    }

    AttachmentInfo saveWithByte(String name, String ownerId, ownerName, byte[] data) {
        def fileHash = EncoderUtil.sha256(data);
        def attInfo = new AttachmentInfo(name: name, ownerId: ownerId, ownerName: ownerName, fileSize: data.length);
        def existFile = getFile(fileHash)
        if (existFile) {
            existFile.refCount++;
            attInfo.fileId = existFile.fileId;
        } else {
            //fileId指定用fileHash
            AttachmentFile newFile = generalRepository.saveEntity(
                    new AttachmentFile(fileId: fileHash, data: data, refCount: 1));
            attInfo.fileId = newFile.fileId;
            //同一事务可能多次引用同一个file
            generalRepository.flush();
        }
        saveEntity(attInfo)
    }

    AttachmentFile getFile(String fileId) {
        generalRepository.get(AttachmentFile, fileId);
    }

    InfoAndFile getInfoAndFile(String ownerId, String fileId) {
        log.info "getInfoAndFile $ownerId $fileId"
        new InfoAndFile([info: findFirst([eq: [['ownerId', ownerId], ['fileId', fileId]]]),
                         file: generalRepository.get(AttachmentFile, fileId)])
    }

    void deleteInfoByOwners(List ownerList) {
        log.info "deleteInfoByOwners $ownerList"
        if (ownerList)
            list(['in': [['ownerId', ownerList]]]).each { info ->
                deleteInfo(info)
            }
    }

    void deleteInfoByOwnerAndFileId(String ownerId, String fileId) {
        log.info "deleteInfoByOwnerAndFileId $ownerId $fileId"
        def info = findFirst([eq: [['ownerId', ownerId], ['fileId', fileId]]])
        if (info)
            deleteInfo(info)
    }

    void deleteInfo(AttachmentInfo info) {
        generalRepository.delete(info)
        def file = generalRepository.get(AttachmentFile, info.fileId);
        if (file.refCount == 1)
            generalRepository.delete(file)
        else
            file.refCount--;
    }

    @ReadOnly
    List queryByOwner(String ownerId) {
        log.info "queryByOwner $ownerId"
        list([eq: [['ownerId', ownerId]]]);
    }

    static class InfoAndFile {
        AttachmentInfo info
        AttachmentFile file
    }
}
