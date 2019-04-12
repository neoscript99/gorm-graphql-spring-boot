import {
  createApolloClient,
  DomainGraphql,
  MobxDomainStore,
  DomainService
} from 'oo-graphql-service'
import config from '../utils/config'
import MenuService from '../services/MenuService';
import InitService from './InitService';

const uri = config.graphqlUri;
const defaultVariables = { token: 'gorm-dev-token' }

const apolloClient = createApolloClient(uri)
const domainGraphql: DomainGraphql = new DomainGraphql(apolloClient, defaultVariables);

export const paramService = new DomainService('param', MobxDomainStore, domainGraphql);
export const roleService = new DomainService('role', MobxDomainStore, domainGraphql);
export const menuService = new MenuService(domainGraphql);
export const initService = new InitService(menuService)

