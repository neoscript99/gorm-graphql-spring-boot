import React, { Component, ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { paramService } from '../services'
import { ColumnProps, PaginationConfig } from 'antd/lib/table';
import { timeFormater } from '../utils/dateUtils';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';

const { store: paramStore } = paramService
const columns: Array<ColumnProps<any>> = [
  { title: '参数代码', dataIndex: 'code' },
  { title: '参数名称', dataIndex: 'name' },
  { title: '参数类型', dataIndex: 'type.name' },
  { title: '参数值', dataIndex: 'value' },
  { title: '修改人', dataIndex: 'lastUser.name' },
  { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }];

interface S {
  pagination: PaginationConfig,
  loading: boolean
}

@observer
export default class Param extends Component<any, S> {
  state: S = {
    pagination: {
      pageSize: 2,
      onChange: (page: number, pageSize?: number) => {
        if (pageSize)
          paramStore.pageInfo.pageSize = pageSize
        this.query(page)
      }
    }, loading: false
  }

  query(page: number = 1) {
    let { pagination } = this.state;
    paramStore.pageInfo.currentPage = page
    paramService.listPage({ criteria: {}, orders: ['lastUpdated'] })
      .then((data: ListResult) => {
        pagination.total = data.totalCount
        this.setState({ ...this.state, pagination })
      })
  }

  componentDidMount(): void {
    paramStore.pageInfo.pageSize = this.state.pagination.pageSize
    this.query()
  }

  render(): ReactNode {
    return (
      <Table dataSource={paramStore.pageList}
             columns={columns}
             bordered
             {...this.state}
             rowKey='id'>
      </Table>)
  }
}
