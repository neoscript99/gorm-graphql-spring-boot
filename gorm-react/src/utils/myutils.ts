import { ReactNode } from 'react'
import moment from 'moment';
import { PaginationConfig } from 'antd/lib/table';
import { PageInfo } from 'oo-graphql-service/lib/DomainStore';

export function timeFormater(date: Date): ReactNode {
  return moment(date)
    .format('YYYY-MM-DD hh:mm')
}

export function booleanLabel(value: boolean): ReactNode {
  return value ? '是' : '否'
}

export function toPageInfo(pagination: PaginationConfig): PageInfo {
  return {
    currentPage: pagination.current,
    pageSize: pagination.pageSize,
    totalCount: pagination.total
  }
}
