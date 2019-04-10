import { MobxDomainStore } from 'oo-graphql-service'
import MenuStore from './MenuStore';

export default {
  paramStore: new MobxDomainStore(),
  menuStore: new MenuStore()
}

