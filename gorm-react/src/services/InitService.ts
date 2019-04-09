import MenuService from './MenuService';

export default class InitService {
  constructor(private menuService: MenuService) {
  }

  init(): void {
    console.log('InitService')
    this.menuService.getMenuTree('gorm-dev-token')
  }
}
