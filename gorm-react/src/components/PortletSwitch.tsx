import React, { ReactNode } from 'react';
import { Entity } from 'oo-graphql-service';
import PortletLink from './PortletLink';
import PortletTable from './PortletTable';

class PortletSwitch extends React.Component<{ portlet: Entity }> {
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
    return <div style={{ margin: '1rem 0 0 1rem' }}>
      <a id={portlet.id} />
      {this.getPortlet(portlet)}
    </div>
  }
}

export default PortletSwitch;
