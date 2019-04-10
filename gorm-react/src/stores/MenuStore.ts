import { observable } from 'mobx';

export type MenuNode = {
  menu: any;
  subMenus: Array<MenuNode>
}

class MenuStore  {
  @observable
  menuTree: MenuNode = { menu: {}, subMenus: new Array<MenuNode>() };
}

export default MenuStore
