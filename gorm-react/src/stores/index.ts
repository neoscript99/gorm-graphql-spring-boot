import { MobxStore } from 'oo-graphql-service'
import MenuStore from './MenuStore';

export default {
  paramStore: new MobxStore(),
  menuStore: new MenuStore()
}

