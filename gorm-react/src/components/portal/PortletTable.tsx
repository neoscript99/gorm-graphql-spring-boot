import React from 'react';
import { DomainService, Entity, MobxDomainStore } from 'oo-graphql-service';
import { portletTableService } from '../../services';
import { observer } from 'mobx-react';
import { Card, Table } from 'antd';
import Portlet, { PortletProps, PortletState } from './Portlet';
import { ColumnProps } from 'antd/lib/table';
import { commonColumnRenders } from '../../utils/myutils';

export interface PortletColumnProps extends ColumnProps<Entity> {
  renderFun?: string
}

export interface PortletTableState extends PortletState {
  columns: PortletColumnProps[]
}

@observer
class PortletTable extends Portlet<PortletProps, PortletTableState> {

  render() {
    if (!(this.state && this.state.portlet && this.state.data))
      return null

    const { portlet: table, data } = this.state
    const columns: PortletColumnProps[] = table.columns && JSON.parse(table.columns)
    columns.forEach(col => col.render = col.renderFun && commonColumnRenders[col.renderFun])
    return (
      <Card title={table.portletName}>
        <Table dataSource={data} columns={columns}
               rowKey={table.rowKey}
               pagination={{ pageSize: table.pageSize }} bordered />
      </Card>
    )
  }

  get portletService(): DomainService<MobxDomainStore> {
    return portletTableService;
  }
}

export default PortletTable;
