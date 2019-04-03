import stores from '../stores'

class InitService {

  init (): void {
    console.log('InitService')
    stores.menuStore.listAll({ order: ['seq'] })
  }
}

export default new InitService()
