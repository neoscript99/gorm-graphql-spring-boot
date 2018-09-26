package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.AttachmentFile
import neo.script.gorm.general.domain.sys.AttachmentInfo
import neo.script.util.EncoderUtil
import org.springframework.stereotype.Service

@Service
class AttachmentService extends AbstractService<AttachmentInfo> {

    AttachmentInfo saveWithFile(File file) {
        if (!file.isFile())
            throw new RuntimeException("$file.path,该文件不存在")

        return saveWithByte(file.name, file.length(), file.getBytes());
    }

    AttachmentInfo saveWithByte(String name, String ownerId, byte[] data, String fileId = null) {
        def fileHash = EncoderUtil.sha256(data);

        def existInfo = findByHash(fileHash)
        if (existInfo)
            return existInfo;

        AttachmentFile attachFile = generalRepository.saveEntity(new AttachmentFile(fileId: fileId ?: fileHash, data: data));
        def attInfo = new AttachmentInfo(name: name, fileId: attachFile.fileId, ownerId: ownerId, fileSize: data.length, fileHash: fileHash);
        return saveEntity(attInfo)
    }

    AttachmentInfo findByHash(String hash) {
        findFirst([eq: [['fileHash', hash]]])
    }

    AttachmentFile getFile(String fileId) {
        generalRepository.get(AttachmentFile, fileId);
    }
}
