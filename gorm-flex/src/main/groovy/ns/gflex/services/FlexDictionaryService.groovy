/**
 *
 */
package ns.gflex.services

import neo.script.gorm.general.domain.sys.*
import ns.gflex.services.base.GFlexService
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.stereotype.Service

/**
 * @author neo
 *
 */
@Service
class FlexDictionaryService extends GFlexService {
    Boolean isSystemAdmin() {
        return list([eq: [['user', sessionUser]], role: [eq: [
                [
                        'roleCode',
                        'Administrators']
        ]]], UserRole)
    }

    List getDepartmentList() {
        listEnabled(null, Department)
    }

    List getAllUserList() {
        listEnabled([eq: [['editable', true]]], User)
    }

    List getAllRoleList() {
        listEnabled(null, Role)
    }

    List getUserRoles(String account) {
        UserRole.findAllByUser(User.findByAccount(account))*.role
    }

    List getParamList() {
        Param.list()
    }

    List getParamTypeList() {
        ParamType.list()
    }

    List getLabelList() {
        logHost("getLabelList")
        Label.withCriteria {
            projections {
                groupProperty 'label', 'label'
                count 'id', 'numbers'
            }
            order 'numbers', 'desc'
            maxResults 200
            resultTransformer CriteriaSpecification.ALIAS_TO_ENTITY_MAP
        }*.label
    }

    List getHotLabelList() {
        logHost("getHotLabelList")
        Label.withCriteria {
            projections {
                groupProperty 'label', 'label'
                count 'id', 'numbers'
            }
            order 'numbers', 'desc'
            maxResults 30
            resultTransformer CriteriaSpecification.ALIAS_TO_ENTITY_MAP
        }
    }

    @Override
    protected Class getDomainClass() {
        return null
    }

    @Override
    protected void authorityCheck() {
        //本service执行时系统还处于未登录
    }
}
