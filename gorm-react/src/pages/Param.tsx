import React, { Component, ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { paramService } from '../services'
import { ColumnProps, PaginationConfig } from 'antd/lib/table';
import { timeFormater, toPageInfo } from '../utils/myutils';
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
        this.state.pagination.current = page;
        this.query()
      },
      onShowSizeChange: (current, size) => {
        this.state.pagination.pageSize = size
        this.state.pagination.current = 1;
        this.query()
      },
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total) => `共 ${total} 记录  `
    }, loading: false
  }

  query() {
    this.setState({ loading: true })
    let { pagination } = this.state;
    paramService.listPage({ criteria: {}, pageInfo: toPageInfo(pagination), orders: [['lastUpdated', 'desc',]] })
      .then((data: ListResult) => {
        pagination.total = data.totalCount
        this.setState({ pagination, loading: false })
      })
  }

  componentDidMount(): void {
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
