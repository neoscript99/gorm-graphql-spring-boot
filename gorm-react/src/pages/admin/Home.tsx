import React, { Component } from 'react'
import { Button, Divider, Icon, Layout } from 'antd'
import { observer } from 'mobx-react'
import { Route, Switch } from 'react-router-dom';
import Welcome from './Welcome';
import Role from './Role';
import User from './User';
import Note from './Note';
import Param from './Param';
import Profile from './Profile';
import MenuTree from '../../components/MenuTree';
import { menuService } from '../../services';
import { Location } from 'history';

const {
  Header, Content, Footer, Sider
} = Layout

interface P {
  location: Location
}

@observer
class Home extends Component<P, { collapsed: boolean }> {
  state = {
    collapsed: false,
  }

  onCollapse = (collapsed: boolean) => {
    console.log(collapsed)
    this.setState({ collapsed })
  }

  render() {
    const pathPrefix = '/admin/'
    const { store: menuStore } = menuService;
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className='flex-row'
                style={{
                  fontWeight: 'bolder',
                  fontSize: '1.5rem',
                  color: 'white',
                  justifyContent: 'space-between',
                  alignItems: 'center'
                }}>
          <div><Icon type='tool'/><Divider type="vertical" />系统设置</div>
          <Button type='primary' shape='circle' icon='home' href='/' />
        </Header>
        <Layout>
          <Sider
            collapsible
            collapsed={this.state.collapsed}
            onCollapse={this.onCollapse}
            theme="light"
          >
            <MenuTree rootMenu={menuStore.menuTree} pathPrefix={pathPrefix} />
          </Sider>
          <Layout>
            <Content style={{ margin: '1rem', padding: 24, background: '#fff', height: '100%', minHeight: 360 }}>
              <Switch>
                <Route path={`${pathPrefix}Role/`} component={Role} />
                <Route path="/User/" component={User} />
                <Route path="/Note/" component={Note} />
                <Route path={`${pathPrefix}Param/`} component={Param} />
                <Route path="/Profile/" component={Profile} />
                <Route component={Welcome} />
              </Switch>
            </Content>
            <Footer style={{ textAlign: 'center' }}>
              顶点软件 ©2019
            </Footer>
          </Layout>
        </Layout>
      </Layout>
    )
  }
}

export default Home
