import React, { Component } from 'react'
import './App.css'
import Home from './pages/admin/Home'
import { HashRouter as Router, Route, Switch } from 'react-router-dom';
import Login, { Logout } from './pages/Login';
import Portal from './pages/Portal';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/admin/" component={Home} />
          <Route path="/login/" component={Login} />
          <Route path="/logout/" component={Logout} />
          <Route path="/portal/:portalCode" component={Portal} />
          <Route component={Portal} />
        </Switch>
      </Router>
    )
  }
}

export default App
