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
export const roleService = new DomainService('role', MobxDomainStore, domainGraphql);
export const portalService = new DomainService('portal', MobxDomainStore, domainGraphql);
export const portletService = new DomainService('portlet', MobxDomainStore, domainGraphql);
export const portletLinkService = new DomainService('portletLink', MobxDomainStore, domainGraphql);
export const portletTableService = new PortletTableService('portletTable', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);
export const initService = new InitService(menuService)

