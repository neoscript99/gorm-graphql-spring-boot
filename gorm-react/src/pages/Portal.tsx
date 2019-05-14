import React, { Component } from 'react'
import { Layout } from 'antd'
import { observer } from 'mobx-react'
import { Route, Switch } from 'react-router-dom';
import Welcome from './Welcome';
import Role from './Role';
import User from './User';
import Note from './Note';
import Param from './Param';
import Profile from './Profile';
import MenuTree from '../components/MenuTree';
import { menuService } from '../services';

const {
  Header, Content, Footer, Sider
} = Layout

@observer
class Portal extends Component<any, { collapsed: boolean }> {
  state = {
    collapsed: false,
  }

  onCollapse = (collapsed: boolean) => {
    console.log(collapsed)
    this.setState({ collapsed })
  }

  render() {
    const { store: menuStore } = menuService;
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className="portal_layout">
          <div className="logo" />
          信息门户
        </Header>
        <Content className="portal_body">Content</Content>
        <Footer className="portal_layout">Footer</Footer>
      </Layout>
    )
  }
}

export default Portal
