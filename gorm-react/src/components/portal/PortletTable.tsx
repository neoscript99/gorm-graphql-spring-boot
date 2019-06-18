import React from 'react';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { portletTableService } from '../../services';
import { observer } from 'mobx-react';
import { Card, Table } from 'antd';
import Portlet from './Portlet';


@observer
class PortletTable extends Portlet {

  render() {
    if (!(this.state && this.state.columns && this.state.data))
      return null

    const { portlet: table, columns, data } = this.state
    return (
      <Card title={table.portletName}>
        <Table dataSource={data} columns={columns}
               rowKey={table.rowKey}
               pagination={{ pageSize: 5 }} bordered />
      </Card>
    )
  }

  get portletService(): DomainService<MobxDomainStore> {
    return portletTableService;
  }
}

export default PortletTable;
