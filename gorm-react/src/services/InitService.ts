import stores from '../stores'

class InitService {

  init (): void {
    console.log('InitService')
    stores.menuStore.listAll({})
  }
}

export default new InitService()
