import React, { Fragment } from 'react';
import { portalRowRelService } from '../../services';
import { observer } from 'mobx-react';
import { Row } from 'antd';
import { Entity } from 'oo-graphql-service';
import { clearEntity } from '../../utils/myutils';
import PortalCol from './PortalCol';
import { PortletMap } from './PortletSwitch';

interface P {
  portal: Entity
  customerPortletMap: PortletMap
}

@observer
class PortalRows extends React.Component<P> {

  render() {
    if (!portalRowRelService.store.allList)
      return null;
    const { portal, customerPortletMap } = this.props;
    const relList = portalRowRelService.store.allList
      .filter(value => value.portal.id === portal.id)

    return <Fragment>
      {relList.map(rel => <Row {...clearEntity(rel.row, 'rowName', 'rowOrder', 'cols')} key={rel.id}>
        {
          rel.row.cols
            .slice()
            .sort((a: Entity, b: Entity) => a.colOrder - b.colOrder)
            .map((col: Entity) => <PortalCol key={col.id} col={col} customerPortletMap={customerPortletMap} />)
        }
      </Row>)}
    </Fragment>
  }
}

export default PortalRows;
