import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { noteService } from '../../services'
import { ColumnProps } from 'antd/lib/table';
import { Entity, EntityPageList, MobxDomainStore, DomainService, commonColumns } from 'oo-graphql-service';

const { store } = noteService
const columns: Array<ColumnProps<Entity>> = [
  { title: '标题', dataIndex: 'title' },
  { title: '内容', dataIndex: 'content' },
  { title: '附件数', dataIndex: 'attachNum' },
  commonColumns.lastUser,
  commonColumns.lastUpdated,
];


@observer
export default class Note extends EntityPageList {

  render(): ReactNode {
    return (
      <Table dataSource={store.pageList}
             columns={columns}
             bordered
             {...this.tableProps}
             rowKey='id'>
      </Table>)
  }

  get domainService(): DomainService<MobxDomainStore> {
    return noteService;
  }
}
