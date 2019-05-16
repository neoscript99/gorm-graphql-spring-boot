import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { userService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { commonColumns } from '../../utils/myutils';
import EntityPageList from '../../components/EntityPageList';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import DomainService from 'oo-graphql-service/lib/DomainService';
import { Entity } from 'oo-graphql-service';

const { store } = userService
const columns: Array<ColumnProps<Entity>> = [
  { title: '姓名', dataIndex: 'name' },
  { title: '帐号', dataIndex: 'account' },
  { title: '所属机构', dataIndex: 'dept.name' },
  commonColumns.enabled,
  commonColumns.editable,
  commonColumns.lastUser,
  commonColumns.lastUpdated,
];


@observer
export default class User extends EntityPageList {

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
    return userService;
  }
}
