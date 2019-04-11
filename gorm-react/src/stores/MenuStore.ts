import { observable } from 'mobx';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { Entity } from 'oo-graphql-service';

export interface MenuNode {
  menu: Entity;
  subMenus: Array<MenuNode>
}

class MenuStore extends MobxDomainStore {
  @observable
  menuTree: MenuNode = { menu: {}, subMenus: new Array<MenuNode>() };
}

export default MenuStore
