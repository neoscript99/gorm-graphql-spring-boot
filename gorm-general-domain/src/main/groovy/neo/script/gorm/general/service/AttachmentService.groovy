package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.AttachmentFile
import neo.script.gorm.general.domain.sys.AttachmentInfo
import neo.script.util.EncoderUtil
import org.springframework.stereotype.Service

@Service
class AttachmentService extends AbstractService<AttachmentInfo> {

    AttachmentInfo saveWithFile(File file, String ownerId) {
        if (!file.isFile())
            throw new RuntimeException("$file.path,该文件不存在")

        return saveWithByte(file.name, ownerId, file.getBytes());
    }

    AttachmentInfo saveWithByte(String name, String ownerId, byte[] data, String fileId = null) {
        def fileHash = EncoderUtil.sha256(data);
        def attInfo = new AttachmentInfo(name: name, ownerId: ownerId, fileSize: data.length);
        def existFile = findFileByHash(fileHash)
        if (existFile) {
            existFile.refCount++;
            attInfo.fileId = existFile.fileId;
        } else {
            //fileId需指定，如果未传入用fileHash
            AttachmentFile newFile = generalRepository.saveEntity(
                    new AttachmentFile(fileId: fileId ?: fileHash, fileHash: fileHash, data: data, refCount: 1));
            attInfo.fileId = newFile.fileId;
        }
        saveEntity(attInfo)

    }

    AttachmentFile findFileByHash(String hash) {
        generalRepository.findFirst(AttachmentFile, [eq: [['fileHash', hash]]])
    }

    AttachmentFile getFile(String fileId) {
        generalRepository.get(AttachmentFile, fileId);
    }
}
