import { observable } from 'mobx';
import { Entity, MobxDomainStore } from 'oo-graphql-service';

export interface MenuNode {
  menu: Entity;
  subMenus: Array<MenuNode>
}

class MenuStore extends MobxDomainStore {
  @observable
  menuTree: MenuNode = { menu: {}, subMenus: new Array<MenuNode>() };
}

export default MenuStore
