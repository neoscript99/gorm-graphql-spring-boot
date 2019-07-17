import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { portletService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { Entity, EntityPageList, MobxDomainStore, DomainService, commonColumns } from 'oo-graphql-service';

const { store } = portletService
const columns: Array<ColumnProps<Entity>> = [
  { title: '控件名称', dataIndex: 'portletName' },
  { title: '所属门户', dataIndex: 'portal.portalName' },
  { title: '控件类型', dataIndex: 'type' },
  commonColumns.lastUpdated
];


@observer
export default class PortletManage extends EntityPageList {

  render (): ReactNode {
    return (
      <Table dataSource={store.pageList}
        columns={columns}
        bordered
        {...this.tableProps}
        rowKey='id'>
      </Table>)
  }

  get domainService (): DomainService<MobxDomainStore> {
    return portletService;
  }
}
