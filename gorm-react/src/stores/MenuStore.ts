import { observable } from 'mobx';
import { MobxStore } from 'oo-graphql-service';

export type MenuNode = {
  menu: any;
  subMenus: Array<MenuNode>
}

class MenuStore extends MobxStore {
  @observable
  menuTree: MenuNode = { menu: {}, subMenus: new Array<MenuNode>() };
}

export default MenuStore
