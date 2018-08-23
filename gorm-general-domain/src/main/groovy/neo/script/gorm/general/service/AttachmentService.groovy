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

        def bytes = file.getBytes();
        def fileHash = EncoderUtil.sha256(bytes);

        def existInfo = findByHash(fileHash)
        if (existInfo)
            return existInfo;

        AttachmentFile attachFile = generalRepository.saveEntity(new AttachmentFile(data: file.bytes));
        def attInfo = new AttachmentInfo(name: file.name, fileId: attachFile.id, fileSize: file.length(), fileHash: fileHash);
        return saveEntity(attInfo)
    }

    AttachmentInfo findByHash(String hash) {
        findFirst([eq: [['fileHash', hash]]])
    }

    AttachmentFile getFile(String fileId) {
        generalRepository.get(AttachmentFile, fileId);
    }
}
