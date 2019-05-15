import React, { PureComponent, ReactNode } from 'react';
import { Entity } from 'oo-graphql-service';
import PortletLink from './PortletLink';
import PortletTable from './PortletTable';
import { Card } from 'antd';

class PortletSwitch extends PureComponent<{ portlet: Entity }> {
  getPortlet(portlet: Entity): ReactNode {

    switch (portlet.type) {
      case 'PortletLink':
        return <PortletLink portlet={portlet} />
      case 'PortletTable':
        return <PortletTable portlet={portlet} />
      default:
        return <div>{portlet.portletName}对应的{portlet.type}控件不存在，请联系管理员</div>
    }

  }

  render() {
    const { portlet } = this.props;
    return <Card style={{ width: 300, margin: '1rem 0 0 1rem' }} title={portlet.portletName}
                 extra={<a id={portlet.id} />}>
      {this.getPortlet(portlet)}
    </Card>
  }
}

export default PortletSwitch;
