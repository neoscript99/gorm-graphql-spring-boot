import React, { Component } from 'react';
import { clearEntity } from '../utils/myutils';
import { Col } from 'antd';
import { Entity } from 'oo-graphql-service';
import { portletColRelService } from '../services';
import PortletSwitch from './PortletSwitch';

interface P {
  col: Entity
}

class PortalCol extends Component<P> {
  render() {
    if (!portletColRelService.store.allList)
      return null;
    const { col } = this.props;
    return (
      <Col {...clearEntity(col, 'colOrder', 'colOffset', 'row', 'style')} order={col.colOrder}
           offset={col.colOffset} style={JSON.parse(col.style)}>
        {portletColRelService.store.allList.filter(rel => rel.col.id === col.id)
          .map(rel =>
            <PortletSwitch key={rel.portlet.id} portlet={rel.portlet} />)}
      </Col>
    );
  }
}

export default PortalCol;
