import {
  createApolloClient,
  DomainGraphqlGorm,
  DomainGraphql,
  DomainService,
  MessageStore,
  MobxMessageStore
} from 'oo-graphql-service'
import ApolloClient from 'apollo-client';
import { NormalizedCacheObject } from 'apollo-cache-inmemory';
import config from '../utils/config'
import MenuService from '../services/MenuService';
import mboxStore from '../stores'
import InitService from './InitService';

const uri = config.graphqlUri;
const defaultVariables = { token: 'gorm-dev-token' }

const apolloClient: ApolloClient<NormalizedCacheObject> = createApolloClient({ uri })
const domainGraphql: DomainGraphql = new DomainGraphqlGorm(apolloClient, defaultVariables);
const messageStore: MessageStore = new MobxMessageStore();


export const paramService = new DomainService('param', mboxStore.paramStore, messageStore, domainGraphql);
export const menuService = new MenuService(mboxStore.menuStore, domainGraphql, defaultVariables);
export const initService = new InitService(menuService)

