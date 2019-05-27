import {
  createApolloClient,
  DomainGraphql,
  MobxDomainStore,
  DomainService
} from 'oo-graphql-service'
import config from '../utils/config'
import MenuService from '../services/MenuService';
import PortletTableService from './PortletTableService';
import UserService from './UserService';

const uri = config.graphqlUri;
//用户登录后更新token
export const graphqlVars = { token: '' }//{ token: 'gorm-dev-token' }

const apolloClient = createApolloClient(uri)
const domainGraphql: DomainGraphql = new DomainGraphql(apolloClient, graphqlVars);

export const paramService = new DomainService('param', MobxDomainStore, domainGraphql);
export const noteService = new DomainService('note', MobxDomainStore, domainGraphql);
export const roleService = new DomainService('role', MobxDomainStore, domainGraphql);
export const portalService = new DomainService('portal', MobxDomainStore, domainGraphql);
export const portletService = new DomainService('portlet', MobxDomainStore, domainGraphql);
export const portletLinkService = new DomainService('portletLink', MobxDomainStore, domainGraphql);
export const portletTableService = new PortletTableService('portletTable', MobxDomainStore, domainGraphql);
export const portletDbService = new DomainService('portalDb', MobxDomainStore, domainGraphql);
export const portletDbQueryService = new DomainService('portalDbQuery', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);


export const userService = new UserService(menuService, domainGraphql);
userService.tryLocalLogin();
