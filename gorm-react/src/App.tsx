import React, { Component } from 'react'
import './App.css'
import Home from './pages/admin/Home'
import { HashRouter as Router, Route, Switch, Redirect } from 'react-router-dom';
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
          <Route path="/" component={Portal} />
        </Switch>
      </Router>
    )
  }
}

export default App
