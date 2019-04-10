import React, { Component, ReactNode } from 'react'
import { inject, observer } from 'mobx-react';
import { Table } from 'antd';
import { DomainStore } from 'oo-graphql-service';
import { paramService } from '../services'
import { ColumnProps, PaginationConfig } from 'antd/lib/table';
import { timeFormater } from '../utils/dateUtils';

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

@inject('paramStore')
@observer
export default class Param extends Component<{ paramStore: DomainStore }, S> {
  state: S = { pagination: { position: 'both', pageSize: 2 }, loading: false }

  componentDidMount(): void {
    let { pagination } = this.state;
    this.props.paramStore.pageInfo.pageSize = this.state.pagination.pageSize
    paramService.listPage({ orders: ['lastUpdated'] })
      .then(data => {
        if (data) {
          pagination.total = data.totalCount
          this.setState({ ...this.state, pagination })
        }
      })
  }

  render(): ReactNode {
    return (
      <Table dataSource={this.props.paramStore.allList}
             columns={columns}
             bordered
             {...this.state}
             rowKey='id'>
      </Table>)
  }
}
