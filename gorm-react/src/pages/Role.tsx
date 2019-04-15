import React, { ReactNode } from 'react'
import { Table } from 'antd';
import EntityList from '../components/EntityList';
import { roleService } from '../services';
import { ColumnProps } from 'antd/lib/table';
import { booleanLabel, timeFormater } from '../utils/myutils';
import { Entity } from 'oo-graphql-service';
import { observer } from 'mobx-react';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import DomainService, { ListOptions } from 'oo-graphql-service/lib/DomainService';

const columns: Array<ColumnProps<Entity>> = [
  { title: '角色名', dataIndex: 'roleName' },
  { title: '角色代码(unique)', dataIndex: 'roleCode' },
  { title: '启用', dataIndex: 'enabled', render: booleanLabel },
  { title: '可编辑', dataIndex: 'editable', render: booleanLabel },
  { title: '描述', dataIndex: 'description' },
  { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }];

@observer
class Role extends EntityList {

  render(): ReactNode {
    return (
      <Table dataSource={roleService.store.allList}
             columns={columns}
             bordered
             rowKey='id'
             {...this.tableProps}>
      </Table>)
  }

  get domainService(): DomainService<MobxDomainStore> {
    return roleService;
  }

  protected get queryParam(): ListOptions {
    return { orders: [['lastUpdated', 'desc']] };
  }
}

export default Role;
