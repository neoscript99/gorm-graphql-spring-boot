import React, { Component } from 'react'
import './App.css'
import Home from './pages/Home'
import mobxStores from './stores'
import initService from './services/InitService'
import { Provider } from 'mobx-react'

initService.init()

class App extends Component {
  render () {
    return (
      <Provider {...mobxStores}>
        <Home menuStore={mobxStores.menuStore} />
      </Provider>
    )
  }
}

export default App
