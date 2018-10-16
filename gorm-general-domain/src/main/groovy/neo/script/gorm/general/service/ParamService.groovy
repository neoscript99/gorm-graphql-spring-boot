package neo.script.gorm.general.service

import neo.script.gorm.general.domain.sys.Param
import org.springframework.stereotype.Service

/**
 * 参数管理
 * @since 2018-10-16
 * @author wangchu
 */
@Service
class ParamService extends AbstractService<Param> {
    String getValue(String typeCode, String code) {
        log.info("ParamService.getByTypeAndCode $typeCode $code");
        findFirst([eq: [['type.code', typeCode], ['code', code]]])?.value
    }
}
