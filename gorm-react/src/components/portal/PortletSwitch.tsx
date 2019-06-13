import React, { ReactNode } from 'react';
import { Entity } from 'oo-graphql-service';
import PortletLink from './PortletLink';
import PortletTable from './PortletTable';
import { Card, Icon } from 'antd'
import PortletLiveList from './PortletLiveList';

class PortletSwitch extends React.Component<{ portlet: Entity }> {
  getPortlet (portlet: Entity): ReactNode {
    switch (portlet.type) {
      case 'PortletLink':
        return <PortletLink portlet={portlet} />
      case 'PortletTable':
        return <PortletTable portlet={portlet} />
      case 'PortletLiveList':
        return <PortletLiveList portlet={portlet} />
      default:
        return <Card title={portlet.portletName}
          actions={[<Icon type="setting" />, <Icon type="edit" />, <Icon type="ellipsis" />]}>
          <Card.Meta
            avatar={<Icon type="close" style={{ color: 'red', fontSize: '2rem' }} />}
            title="控件不存在，请联系管理员"
            description={portlet.type}
          />
        </Card>
    }

  }

  render () {
    const { portlet } = this.props;
    return <div style={{ margin: '1rem 0 0 1rem' }}>
      <a id={portlet.id} />
      {this.getPortlet(portlet)}
    </div>
  }
}

export default PortletSwitch;
