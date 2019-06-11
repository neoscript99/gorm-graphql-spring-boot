import React from 'react'
import { Menu, Icon, Layout, BackTop, Col, Row } from 'antd'
import { observer } from 'mobx-react'
import { portalService, userService } from '../services';
import { Link, Redirect } from 'react-router-dom';
import PortalRows from '../components/PortalRows';
import PortalSider from '../components/PortalSider';

const {
  Header, Content, Footer
} = Layout

@observer
class Portal extends React.Component {

  render() {
    const { store: portalStore } = portalService;
    if (!userService.store.currentItem.id)
      return (<Redirect to="/login/" />)
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className="portal_layout">
          <div className="logo" />
          <Menu theme="light" mode="horizontal">
            {
              portalStore.allList && portalStore.allList.map(portal =>
                <Menu.Item key={portal.id}>
                  <Icon type={portal.portalIcon} />
                  <Link to='#' style={{ display: 'inline' }}>{portal.portalName}</Link>
                </Menu.Item>)
            }
            <Menu.Item key='admin'>
              <Icon type='tool' />
              <Link to='/admin/' style={{ display: 'inline' }}>系统设置</Link>
            </Menu.Item>
            <Menu.Item key='login'>
              <Icon type='user' />
              <Link to='/logout/' style={{ display: 'inline' }}>退出登录</Link>
            </Menu.Item>
          </Menu>
        </Header>
        <Layout>
          {portalStore.currentItem && <PortalSider portal={portalStore.currentItem} />}
          <Content>
            {portalStore.currentItem && <PortalRows portal={portalStore.currentItem} />}
          </Content>
        </Layout>
        <Footer className="portal_layout"
                style={{
                  height: '1.5rem',
                  justifyContent: 'space-around',
                  alignItems: 'center'
                }}>
          <div style={{ marginTop: '0.5rem' }}>顶点软件</div>
          <BackTop />
        </Footer>
      </Layout>
    )
  }
}

export default Portal
