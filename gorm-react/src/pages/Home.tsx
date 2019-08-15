import React, { Component } from 'react'
import { Icon, Button, Layout, Divider } from 'antd'
import { observer } from 'mobx-react'
import * as H from 'history';
import { match as Match, Redirect, Route, Switch } from 'react-router-dom';
import { MenuTree, Welcome, Role, User, Note, Param, Profile, MenuEntity } from 'oo-graphql-service';
import { adminRequiredServices } from '../services';
import { PortalDbManage, PortalDbQueryManage, PortalManage, PortletManage } from './admin'
import { config } from '../utils';

const { menuService, userService } = adminRequiredServices;

const {
  Header, Content, Footer, Sider
} = Layout

interface P {
  history: H.History
  location: H.Location
  match: Match
}

@observer
export class Home extends Component<P, { collapsed: boolean }> {
  state = {
    collapsed: false,
  }

  onCollapse = (collapsed: boolean) => {
    console.log(collapsed)
    this.setState({ collapsed })
  }

  onMenuClick = (menu: MenuEntity) => {
    this.props.history.push(`${this.props.match.path}${menu.app}`)
  }

  logout() {
    if (config.serverLogout) {
      userService.clearLoginInfoLocal()
      userService.changeCurrentItem({})
      window.location.href = `${config.serverRoot}logout`;
    } else
      this.props.history.push('/logout/')
  }

  render() {

    const { location, match } = this.props;
    const pathPrefix = match.path
    const { store: menuStore } = menuService;
    if (!userService.store.currentItem.account) {
      userService.store.lastRoutePath = location.pathname
      return (<Redirect to="/login/" />)
    }
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className='page-head'>
          <div className="page-head-logo" />
          <div>
            <Icon type='tool' />
            <Divider type="vertical" />
            <span>系统设置</span>
            <Divider type="vertical" />
            <Button type='primary' shape='circle' icon='home' onClick={() => this.props.history.push('/')} />
          </div>
        </Header>
        <Layout hasSider>
          <Sider
            collapsible
            collapsed={this.state.collapsed}
            onCollapse={this.onCollapse}
            theme="light"
            className='page-side'
          >
            <MenuTree rootMenu={menuStore.menuTree} menuClick={this.onMenuClick} />
          </Sider>
          <Layout>
            <Content style={{ margin: '1rem', padding: 24, background: '#fff', height: '100%', minHeight: 360 }}>
              <Switch>
                <Route path={`${pathPrefix}Role/`} render={() => <Role services={adminRequiredServices} name='角色' />} />
                <Route path={`${pathPrefix}User/`} render={() => <User services={adminRequiredServices} name='用户' />} />
                <Route path={`${pathPrefix}Note/`} render={() => <Note services={adminRequiredServices} name='通知' />} />
                <Route path={`${pathPrefix}Param/`}
                       render={() => <Param services={adminRequiredServices} name='参数' />} />
                <Route path={`${pathPrefix}Profile/`} render={() => <Profile />} />
                <Route path={`${pathPrefix}PortalManage/`} render={() => <PortalManage name='门户' />} />
                <Route path={`${pathPrefix}PortletManage/`} render={() => <PortletManage name='门户组件' />} />
                <Route path={`${pathPrefix}PortalDbManage/`} render={() => <PortalDbManage name='数据库' />} />
                <Route path={`${pathPrefix}PortalDbQueryManage/`} render={() => <PortalDbQueryManage name='数据库查询' />} />
                <Route render={() => <Welcome />} />
              </Switch>
            </Content>
            <Footer style={{ textAlign: 'center' }}>
              Gorm-React ©2019
            </Footer>
          </Layout>
        </Layout>
      </Layout>
    )
  }
}

export default Home
