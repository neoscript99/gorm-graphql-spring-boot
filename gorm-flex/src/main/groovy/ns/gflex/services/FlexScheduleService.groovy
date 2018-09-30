package ns.gflex.services

import groovy.util.logging.Slf4j
import neo.script.gorm.general.service.AttachmentService
import ns.gflex.services.base.GFlexAttachService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Slf4j
class FlexScheduleService {

    @Autowired
    AttachmentService attachmentService;

    @Scheduled(cron = "0 0 * * * *")
    void cleanTemp() {
        log.info("定时删除一些没有所属对象的附件，正式环境删除一天前的临时附件")
        //def deleteUntil = Date.from(Instant.now().minusSeconds(600));
        def list = attachmentService.list([like: [['ownerId', "${GFlexAttachService.ATTACH_TEMP_ID_PREFIX}%".toString()]],
                                           lt  : [['dateCreated', new Date() - 1]]])
        attachmentService.deleteInfoByOwners(list*.ownerId)
    }
}
