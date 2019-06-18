import React, { Component } from 'react';
import { Avatar, BackTop, Badge, Layout, Menu } from 'antd';

import './LuqiaoPortal.css'
import { observer } from 'mobx-react'
import { portalService } from '../services';
import PortalRows from '../components/portal/PortalRows';
import ContactSearch from './luqiao/portlet/ContactSearch';
import { PortletMap } from '../components/portal/PortletSwitch';
import SystemList from './luqiao/portlet/SystemList';
import NavMenu from './luqiao/NavMenu';
import HeaderIcons from './luqiao/HeaderIcons';
import NoticeBar from './luqiao/NoticeBar';

const customerPortletMap: PortletMap = { ContactSearch, SystemList }

const {
  Header, Content
} = Layout

@observer
class LuqiaoPortal extends Component {
  render() {
    return (
      <Layout id='luqiao' style={{ minHeight: '100vh' }}>
        <Header
          style={{ background: '#fff', padding: 0, height: 'auto' }}>
          <HeaderIcons />
          <NavMenu />
          <NoticeBar />
        </Header>
        <Content style={{ background: '#f4f4f4', padding: '0.5rem' }}>
          {portalService.store.allList && portalService.store.allList
            .filter(p => (p.portalCode as string).startsWith('luqiao'))
            .map(p => <PortalRows key={p.id} portal={p} customerPortletMap={customerPortletMap} />)}
          <BackTop />
        </Content>
      </Layout>
    );
  }
}

export default LuqiaoPortal;
