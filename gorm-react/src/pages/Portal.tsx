import React from 'react'
import { Menu, Icon, Layout, Anchor, BackTop } from 'antd'
import { observer } from 'mobx-react'
import { portalService, portletService, userService } from '../services';
import { Link, Redirect } from 'react-router-dom';
import PortletSwitch from '../components/PortletSwitch';

const {
  Header, Content, Footer, Sider
} = Layout

@observer
class Portal extends React.Component {

  render() {
    const { store: portalStore } = portalService;
    const { store: portletStore } = portletService;
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
          <Sider
            style={{ backgroundColor: '#ffffff', borderTop: 'solid thin #f3f3f3', borderBottom: 'solid thin #f3f3f3' }}>
            <Anchor style={{ backgroundColor: 'inherit', margin: '0.5rem' }}>
              {portletStore.allList && portletStore.allList.map((portlet, index) =>
                <Anchor.Link key={portlet.id} href={'#' + portlet.id} title={`${index + 1}、${portlet.portletName}`} />)}
            </Anchor>
          </Sider>
          <Content className="portal_body">
            {portletStore.allList && portletStore.allList.map(portlet =>
              <PortletSwitch key={portlet.id} portlet={portlet} />)}
          </Content>
        </Layout>
        <Footer className="portal_layout"
                style={{
                  height: '1.5rem',
                  justifyContent: 'space-around',
                  alignItems: 'center'
                }}>
          <div style={{ marginTop: '0.5rem' }}>宁波羽意软件股份有限公司</div>
          <BackTop />
        </Footer>
      </Layout>
    )
  }
}

export default Portal
