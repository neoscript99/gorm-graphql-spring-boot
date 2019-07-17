import React, { ReactNode } from 'react'
import { Table } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { observer } from 'mobx-react';
import {
  Entity,
  EntityList,
  MobxDomainStore,
  DomainService,
  ListOptions,
  booleanLabel,
  timeFormater
} from 'oo-graphql-service';
import { roleService } from '../../services';

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
