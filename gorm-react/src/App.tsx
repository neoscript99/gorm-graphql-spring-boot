import React, { Component } from 'react'
import './App.css'
import { HashRouter as Router, Route, Switch } from 'react-router-dom';
import { Home, Login, Logout, Portal } from './pages';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/admin/" component={Home}  />
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
