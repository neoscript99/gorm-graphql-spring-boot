import stores, { graphqlClient } from '../stores'

class InitService {

  init(): void {
    console.log('InitService')
    stores.menuStore.getMenuTree('gorm-dev-token')
  }
}

export default new InitService()
