import { ReactNode } from 'react'
import moment from 'moment';
import { PaginationConfig } from 'antd/lib/table';
import { PageInfo } from 'oo-graphql-service/lib/DomainStore';

export function timeFormater (date: Date): ReactNode {
  return moment(date)
    .format('YYYY-MM-DD hh:mm')
}

export function booleanLabel (value: boolean): ReactNode {
  return value ? '是' : '否'
}

export function toPageInfo (pagination: PaginationConfig): PageInfo {
  return {
    currentPage: pagination.current,
    pageSize: pagination.pageSize,
    totalCount: pagination.total
  }
}

export const commonColumns = {
  enabled: { title: '是否启用', dataIndex: 'enabled', render: booleanLabel },
  editable: { title: '可编辑', dataIndex: 'editable', render: booleanLabel },
  lastUser: { title: '修改人', dataIndex: 'lastUser.name' },
  lastUpdated: { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }
}