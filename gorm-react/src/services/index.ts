import {
  createApolloClient,
  DomainGraphql,
  MobxDomainStore,
  DomainService
} from 'oo-graphql-service'
import config from '../utils/config'
import MenuService from '../services/MenuService';
import InitService from './InitService';
import PortletTableService from './PortletTableService';

const uri = config.graphqlUri;
const defaultVariables = { token: 'gorm-dev-token' }

const apolloClient = createApolloClient(uri)
const domainGraphql: DomainGraphql = new DomainGraphql(apolloClient, defaultVariables);

export const paramService = new DomainService('param', MobxDomainStore, domainGraphql);
export const noteService = new DomainService('note', MobxDomainStore, domainGraphql);
export const userService = new DomainService('user', MobxDomainStore, domainGraphql);
export const roleService = new DomainService('role', MobxDomainStore, domainGraphql);
export const portalService = new DomainService('portal', MobxDomainStore, domainGraphql);
export const portletService = new DomainService('portlet', MobxDomainStore, domainGraphql);
export const portletLinkService = new DomainService('portletLink', MobxDomainStore, domainGraphql);
export const portletTableService = new PortletTableService('portletTable', MobxDomainStore, domainGraphql);
export const portletDbService = new DomainService('portalDb', MobxDomainStore, domainGraphql);
export const portletDbQueryService = new DomainService('portalDbQuery', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);
export const initService = new InitService(menuService)

