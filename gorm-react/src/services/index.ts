import {
  createApolloClient,
  DomainGraphql,
  MobxDomainStore, PortletDataSourceService,
  LivebosServerService, UserService,
  DomainService, MenuService,
  PortalRequiredServices, LoginInfo, AdminRequiredServices
} from 'oo-graphql-service'
import { config } from '../utils'

const uri = config.graphqlUri;
//用户登录后更新token
export const graphqlVars = { token: '' }


export const apolloClient = createApolloClient({ uri, credentials: 'include' })
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
export const portletTabRelService = new DomainService('portletTabRel', MobxDomainStore, domainGraphql);
export const portletDataSourceService = new PortletDataSourceService('portletDataSource', MobxDomainStore, domainGraphql);
export const portletListViewService = new DomainService('portletListView', MobxDomainStore, domainGraphql);
export const portletCalendarService = new DomainService('portletCalendar', MobxDomainStore, domainGraphql);
export const rdbServerService = new DomainService('rdbServer', MobxDomainStore, domainGraphql);
export const rdbQueryService = new DomainService('rdbQuery', MobxDomainStore, domainGraphql);
export const livebosServerService = new LivebosServerService('livebosServer', MobxDomainStore, domainGraphql);
export const livebosQueryService = new DomainService('livebosQuery', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);

function afterLogin(loginInfo: LoginInfo) {
  graphqlVars.token = loginInfo.token;
  menuService!.getMenuTree()

  portalService.listAll({ criteria: { eq: [['enabled', true]] }, orders: ['seq'] })
  //todo 嵌套属性排序不成功，可能是DetachedCriteria的问题，原来的AbstractHibernateCriteriaBuilder应该是可以的
  portalRowRelService.listAll({ orders: ['portal.seq', 'rowOrder'] })
  portletColRelService.listAll({ orders: ['portletOrder'] })
}

export const userService = new UserService(afterLogin, domainGraphql);
if (config.env === 'dev')
  userService.devLogin('admin', 'gorm-dev-token');
else if (config.casLogin)
  userService.casLogin();
else
  userService.tryLocalLogin();

export const portalServices: PortalRequiredServices = {
  portletColRelService,
  portletDataSourceService,
  portalRowRelService,
  portletTabRelService,
  portletCalendarService,
  portletLinkService,
  portletTableService,
  portletListViewService
}

export const adminRequiredServices: AdminRequiredServices = {
  paramService,
  noteService,
  roleService,
  menuService,
  userService
}
