import React from 'react'
import { Menu, Icon, Layout, BackTop, Col, Row } from 'antd'
import { observer } from 'mobx-react'
import { portalService, userService } from '../services';
import { Link, match, Redirect } from 'react-router-dom';
import PortalRows from '../components/portal/PortalRows';
import PortalSider from '../components/portal/PortalSider';
import { PortletMap } from '../components/portal/PortletSwitch';
import { Entity } from 'oo-graphql-service';

const customerPortletMap: PortletMap = {}
const {
  Header, Content, Footer
} = Layout

interface P {
  match: match<{ portalCode: string }>
}

@observer
class Portal extends React.Component<P> {

  render() {
    const { store: portalStore } = portalService;
    let portal: Entity | undefined;
    if (portalStore.allList) {
      const { portalCode } = this.props.match.params;
      if (portalCode)
        portal = portalStore.allList.find(p => p.portalCode === portalCode)
      else
        portal = portalStore.allList[0]
    }
    if (!userService.store.currentItem.id)
      return (<Redirect to="/login/" push={true} />)
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header className="portal_layout">
          <div className="logo" />
          <Menu theme="light" mode="horizontal">
            {
              portalStore.allList && portalStore.allList.map(portal =>
                <Menu.Item key={portal.id}>
                  <Icon type={portal.portalIcon} />
                  <Link to={`/portal/${portal.portalCode}`} style={{ display: 'inline' }}>{portal.portalName}</Link>
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
          {portal && <PortalSider portal={portal} />}
          <Content style={{ padding: '0.5rem' }}>
            {portal && <PortalRows portal={portal} customerPortletMap={customerPortletMap} />}
          </Content>
        </Layout>
        <Footer className="portal_layout"
                style={{
                  height: '1.5rem',
                  justifyContent: 'space-around',
                  alignItems: 'center'
                }}>
          <div style={{ marginTop: '0.5rem' }}>Gorm-Portal</div>
          <BackTop />
        </Footer>
      </Layout>
    )
  }
}

export default Portal
