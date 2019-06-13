import React, { Fragment } from 'react';
import { observer } from 'mobx-react';
import { Anchor, Layout } from 'antd';
import { Entity } from 'oo-graphql-service';
import { portalRowRelService, portletColRelService } from '../../services';


const {
  Sider
} = Layout

interface P {
  portal: Entity
}

@observer
class PortalSider extends React.Component<P> {

  render() {
    if (!portalRowRelService.store.allList || !portletColRelService.store.allList)
      return null;

    const rowRelList = portalRowRelService.store.allList
      .filter(value => value.portal.id === this.props.portal.id)

    const portletList = rowRelList.flatMap(
      rowRel => rowRel.row.cols
        .slice()
        .sort((a: Entity, b: Entity) => a.colOrder - b.colOrder)
        .flatMap((col: Entity) =>
          portletColRelService.store.allList
            .filter(colRel => colRel.col.id === col.id)
            .map(colRel => colRel.portlet))
    )
    return <Sider
      style={{ backgroundColor: '#ffffff', borderTop: 'solid thin #f3f3f3', borderBottom: 'solid thin #f3f3f3' }}>
      <Anchor style={{ backgroundColor: 'inherit', margin: '0.5rem' }}>
        {portletList.map((portlet, index) =>
          <Anchor.Link key={portlet.id} href={'#' + portlet.id} title={`${index + 1}、${portlet.portletName}`} />)}
      </Anchor>
    </Sider>
  }
}

export default PortalSider;
