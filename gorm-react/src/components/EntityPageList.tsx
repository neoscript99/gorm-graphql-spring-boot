import React from 'react'
import { paramService } from '../services'
import { PaginationConfig, TableProps } from 'antd/lib/table';
import { toPageInfo } from '../utils/myutils';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';
import EntityList, { EntityListState } from './EntityList';
import { Entity } from 'oo-graphql-service';
import { Tag } from 'antd';

abstract class EntityPageList<P =any, S extends EntityListState= EntityListState>
  extends EntityList<P, S> {

  pagination: PaginationConfig = {
    pageSize: 2,
    onChange: this.pageChange.bind(this),
    onShowSizeChange: this.pageSizeChange.bind(this),
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total) => <Tag color="blue">总记录数：{total}</Tag>
  }
  tableProps: TableProps<Entity> = {
    loading: false, pagination: this.pagination
  }

  pageChange(page: number, pageSize?: number): void {
    this.pagination.current = page;
    this.query()
  }

  pageSizeChange(current: number, size: number): void {
    this.pagination.pageSize = size
    this.pagination.current = 1;
    this.query()
  }

  query(): Promise<ListResult> {
    const promise = paramService.listPage({
      criteria: {},
      pageInfo: toPageInfo(this.pagination),
      orders: [['lastUpdated', 'desc',]]
    })
      .then((data: ListResult) => {
        this.pagination.total = data.totalCount
        return data
      })
    return this.showLoading<ListResult>(promise)
  }
}

export default EntityPageList
