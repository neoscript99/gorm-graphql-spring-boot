package ns.gflex.services.base

import neo.script.gorm.general.domain.sys.*

/**
 * 带有label和log功能的服务类
 * @date 2013-7-18
 * @author 王楚
 *
 */
abstract class GFlexLogService extends GFlexService {

    def save(Map map, boolean isMerge = false, Object domain = null) {
        def newEntity = super.save(map, isMerge, domain)
        if (newEntity.id)
            logToDB("${newEntity.class.simpleName} - ${map.id ? 'Modify' : 'Create'} $newEntity", newEntity.id)
        return newEntity
    }

    void deleteByIds(List idList, Object domain = null) {
        logToDB("$ownerName deleteByIds $idList")
        deleteByOwnerList(idList, 'ownerId', Log)
        super.deleteByIds(idList, domain)
    }

    void deleteByOwnerList(List valueList, String ownerIdField, Object domain = null) {
        generalRepository.deleteMatch(domain, ['in': [[ownerIdField, valueList]]])
    }

    void logToDB(String message, Object entityId = null) {
        new Log([ownerId  : entityId,
                 message  : message,
                 account  : sessionAccount,
                 ipAddress: httpRequest ? httpRequest.remoteHost : '@server',
                 ownerName: ownerName]).save()
    }

    String getOwnerName() {
        domainClass.simpleName
    }
}
