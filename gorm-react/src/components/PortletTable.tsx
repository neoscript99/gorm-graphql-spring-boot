import React, { PureComponent } from 'react';
import { Entity } from 'oo-graphql-service';
import { portletTableService } from '../services';
import { observer } from 'mobx-react';

@observer
class PortletTable extends PureComponent<{ portlet: Entity }> {
  componentDidMount() {
    const { portlet } = this.props;
    portletTableService.get(portlet.id)
  }

  render() {
    return (
      <div>

      </div>
    );
  }
}

export default PortletTable;
