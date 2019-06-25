import React, { Component } from 'react';
import { Col } from 'antd';
import { Entity } from 'oo-graphql-service';
import { portletColRelService } from '../../services';
import PortletSwitch, { PortletMap } from './PortletSwitch';
import { observer } from 'mobx-react';

interface P {
  col: Entity
  customerPortletMap: PortletMap
}


@observer
class PortalCol extends Component<P> {
  render() {
    if (!portletColRelService.store.allList)
      return null;
    const { col, customerPortletMap } = this.props;
    return (
      <Col {...JSON.parse(col.exColProps)} style={JSON.parse(col.style)} order={col.colOrder} span={col.span}>
        {portletColRelService.store.allList.filter(rel => rel.col.id === col.id)
          .map(rel =>
            <PortletSwitch key={rel.portlet.id} portlet={rel.portlet} portletMap={customerPortletMap} />)}
      </Col>
    );
  }
}

export default PortalCol;
