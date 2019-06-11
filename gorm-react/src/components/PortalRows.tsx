import React, { Fragment } from 'react';
import { portalRowRelService } from '../services';
import { observer } from 'mobx-react';
import { Row } from 'antd';
import { Entity } from 'oo-graphql-service';
import { clearEntity } from '../utils/myutils';
import PortalCol from './PortalCol';

interface P {
  portal: Entity
}

@observer
class PortalRows extends React.Component<P> {

  render() {
    if (!portalRowRelService.store.allList)
      return null;

    const relList = portalRowRelService.store.allList
      .filter(value => value.portal.id === this.props.portal.id)

    return <Fragment>
      {relList.map(rel => <Row {...clearEntity(rel.row, 'rowOrder', 'cols')} key={rel.id}>
        {
          rel.row.cols
            .slice()
            .sort((a: Entity, b: Entity) => a.colOrder - b.colOrder)
            .map((col: Entity) => <PortalCol key={col.id} col={col} />)
        }
      </Row>)}
    </Fragment>
  }
}

export default PortalRows;
