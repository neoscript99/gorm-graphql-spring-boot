import React, { Component } from 'react'
import './App.css'
import Home from './pages/admin/Home'
import { HashRouter as Router, Route, Switch } from 'react-router-dom';
import Login, { Logout } from './pages/Login';
import Portal from './pages/Portal';
import LuqiaoPortal from './pages/LuqiaoPortal';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/admin/" component={Home} />
          <Route path="/login/" component={Login} />
          <Route path="/logout/" component={Logout} />
          <Route path="/portal" component={Portal} />
          <Route path="/" component={LuqiaoPortal} />
        </Switch>
      </Router>
    )
  }
}

export default App
