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
class Home extends Component<any, { collapsed: boolean }> {
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
        <Header>
          <div className="logo"/>
        </Header>
        <Layout>
          <Sider
            collapsible
            collapsed={this.state.collapsed}
            onCollapse={this.onCollapse}
            theme="light"
          >
            <MenuTree rootMenu={menuStore.menuTree} />
          </Sider>
          <Layout>
            <Content style={{ margin: '1rem', padding: 24, background: '#fff', height: '100%', minHeight: 360 }}>
              <Switch>
                <Route path="/Role/" component={Role} />
                <Route path="/User/" component={User} />
                <Route path="/Note/" component={Note} />
                <Route path="/Param/" component={Param} />
                <Route path="/Profile/" component={Profile} />
                <Route component={Welcome} />
              </Switch>
            </Content>
            <Footer style={{ textAlign: 'center' }}>
              羽意软件 ©2019
            </Footer>
          </Layout>
        </Layout>
      </Layout>
    )
  }
}

export default Home
