import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { paramService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { Entity, EntityPageList, MobxDomainStore, DomainService, commonColumns } from 'oo-graphql-service';

const { store } = paramService
const columns: Array<ColumnProps<Entity>> = [
  { title: '参数代码', dataIndex: 'code' },
  { title: '参数名称', dataIndex: 'name' },
  { title: '参数类型', dataIndex: 'type.name' },
  { title: '参数值', dataIndex: 'value' },
  { title: '修改人', dataIndex: 'lastUser.name' },
  commonColumns.lastUpdated];


@observer
export default class Param extends EntityPageList {

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
    return paramService;
  }
}
