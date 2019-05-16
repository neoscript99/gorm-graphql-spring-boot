import React, { Component } from 'react'
import './App.css'
import Home from './pages/admin/Home'
import { initService } from './services'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Login from './pages/Login';
import Portal from './pages/Portal';

initService.init()

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/admin/" component={Home} />
          <Route path="/login/" component={Login} />
          <Route path="/" component={Portal} />
        </Switch>
      </Router>
    )
  }
}

export default App
