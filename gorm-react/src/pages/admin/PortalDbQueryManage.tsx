import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { rdbQueryService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { Entity, EntityPageList, MobxDomainStore, DomainService, commonColumns } from 'oo-graphql-service';

const { store } = rdbQueryService
const columns: Array<ColumnProps<Entity>> = [
  { title: '数据库名称', dataIndex: 'db.dbName' },
  { title: '查询名称', dataIndex: 'queryName' },
  { title: 'Sql', dataIndex: 'sql' },
  commonColumns.lastUpdated];


@observer
export default class PortalDbQueryManage extends EntityPageList {

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
    return rdbQueryService;
  }
}
