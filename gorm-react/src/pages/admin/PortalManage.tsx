import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { portalService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { booleanLabel, timeFormater } from '../../utils/myutils';
import EntityPageList from '../../components/EntityPageList';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import DomainService from 'oo-graphql-service/lib/DomainService';
import { Entity } from 'oo-graphql-service';

const { store } = portalService
const columns: Array<ColumnProps<Entity>> = [
  { title: '门户名称', dataIndex: 'portalName' },
  { title: '门户图标', dataIndex: 'portalIcon' },
  { title: '是否启用', dataIndex: 'enabled', render: booleanLabel },
  { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }];


@observer
export default class PortalManage extends EntityPageList {

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
    return portalService;
  }
}