import React, { Component } from 'react'
import './App.css'
import Home from './pages/Home'
import { initService } from './services'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Login from './pages/Login';

initService.init()

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/" component={Home} />
          <Route path="/login/" component={Login} />
        </Switch>
      </Router>
    )
  }
}

export default App
