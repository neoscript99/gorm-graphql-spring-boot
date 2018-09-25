package ns.gflex.services

import neo.script.gorm.general.domain.sys.Param
import ns.gflex.services.base.GFlexService
import org.springframework.stereotype.Service;



/**
 * 参数维护
 * @author neo
 * 2012-7-12
 */
@Service
class FlexParamService extends GFlexService{

	def saveValue(Long id,String value){
		Param p=Param.get(id)
		logHost("save Param $p to $value")
		logHost("$sessionUser")
		p.lastUser=sessionUser
		p.value=value
		saving(p)
	}

	@Override
	protected Class getDomainClass() {
		Param.class
	}
}
