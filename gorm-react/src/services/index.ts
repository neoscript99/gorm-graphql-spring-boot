import {
  createApolloClient,
  DomainGraphql,
  MobxDomainStore,
  DomainService
} from 'oo-graphql-service'
import config from '../utils/config'
import MenuService from '../services/MenuService';
import PortletDataSourceService from './PortletDataSourceService';
import UserService, { LoginInfo } from './UserService';
import LivebosServerService from './LivebosServerService';

const uri = config.graphqlUri;
//用户登录后更新token
export const graphqlVars = { token: '' }//{ token: 'gorm-dev-token' }


export const apolloClient = createApolloClient(uri)
export const domainGraphql: DomainGraphql = new DomainGraphql(apolloClient, graphqlVars);

export const paramService = new DomainService('param', MobxDomainStore, domainGraphql);
export const noteService = new DomainService('note', MobxDomainStore, domainGraphql);
export const roleService = new DomainService('role', MobxDomainStore, domainGraphql);
export const portalService = new DomainService('portal', MobxDomainStore, domainGraphql);
export const portalRowRelService = new DomainService('portalRowRel', MobxDomainStore, domainGraphql);
export const portletColRelService = new DomainService('portletColRel', MobxDomainStore, domainGraphql);
export const portletService = new DomainService('portlet', MobxDomainStore, domainGraphql);
export const portletLinkService = new DomainService('portletLink', MobxDomainStore, domainGraphql);
export const portletTableService = new DomainService('portletTable', MobxDomainStore, domainGraphql);
export const portletDsService = new PortletDataSourceService('portletDataSource', MobxDomainStore, domainGraphql);
export const portletListViewService = new DomainService('portletListView', MobxDomainStore, domainGraphql);
export const rdbServerService = new DomainService('rdbServer', MobxDomainStore, domainGraphql);
export const rdbQueryService = new DomainService('rdbQuery', MobxDomainStore, domainGraphql);
export const livebosServerService = new LivebosServerService('livebosServer', MobxDomainStore, domainGraphql);
export const livebosQueryService = new LivebosServerService('livebosQuery', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);

function afterLogin(login: LoginInfo) {
  graphqlVars.token = login.token;
  menuService!.getMenuTree(login.token)

  portalService.listAll({ criteria: { eq: [['enabled', true]] }, orders: ['seq'] })
    .then(listResult => portalService.changeCurrentItem(listResult.results[0]))
  portalRowRelService.listAll({ orders: ['portal.seq', 'rowOrder'] })
  portletColRelService.listAll({orders:['portletOrder']})
}

export const userService = new UserService(afterLogin, domainGraphql);
userService.tryLocalLogin();

