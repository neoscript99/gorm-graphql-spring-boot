import React, { Component, ReactNode } from 'react'
import { Table } from 'antd';
import { paramService } from '../services'
import { ColumnProps, PaginationConfig } from 'antd/lib/table';
import { timeFormater, toPageInfo } from '../utils/myutils';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';
import EntityList, { EntityListState } from './EntityList';

abstract class EntityPageList<P =any, S extends EntityListState= EntityListState>
  extends EntityList<P, S> {
  pagination: PaginationConfig= {
      pageSize: 2,
      onChange: (page: number, pageSize?: number) => {
        this.state.tableProps.pagination.current = page;
        this.query()
      },
      onShowSizeChange: (current, size) => {
        this.state.tableProps.pagination.pageSize = size
        this.state.tableProps.pagination.current = 1;
        this.query()
      },
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total) => `共 ${total} 记录  `
    }

  query(): Promise<ListResult> {
    this.setState({ tableProps: { ...this.state.tableProps, loading: true } })
    let { pagination } = this.state.tableProps;
    return paramService.listPage({ criteria: {}, pageInfo: toPageInfo(pagination), orders: [['lastUpdated', 'desc',]] })
      .then((data: ListResult) => {
        pagination.total = data.totalCount
        return data
      })
      .finally(() =>
        this.setState({ tableProps: { ...this.state.tableProps, pagination, loading: false } })
      )
  }
}

export default EntityPageList
