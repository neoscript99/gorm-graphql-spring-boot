import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { rdbServerService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { commonColumns } from '../../utils/myutils';
import EntityPageList from '../../components/EntityPageList';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import DomainService from 'oo-graphql-service/lib/DomainService';
import { Entity } from 'oo-graphql-service';

const { store } = rdbServerService
const columns: Array<ColumnProps<Entity>> = [
  { title: '数据库名称', dataIndex: 'dbName' },
  { title: '驱动类', dataIndex: 'driverClassName' },
  { title: '连接url', dataIndex: 'url' },
  { title: '用户名', dataIndex: 'username' },
  { title: '密码', dataIndex: 'password' },
  { title: '测试Sql', dataIndex: 'testSql' },
  commonColumns.lastUpdated];


@observer
export default class PortalDbManage extends EntityPageList {

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
    return rdbServerService;
  }
}
