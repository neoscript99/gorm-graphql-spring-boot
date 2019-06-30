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

export const commonColumns = {
  enabled: { title: '是否启用', dataIndex: 'enabled', render: booleanLabel },
  editable: { title: '可编辑', dataIndex: 'editable', render: booleanLabel },
  lastUser: { title: '修改人', dataIndex: 'lastUser.name' },
  lastUpdated: { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }
}

export function clearEntity(entity: any, ...deleteProps: string[]) {
  const { id, lastUpdated, dateCreated, version, errors, ...rest } = entity;
  deleteProps.every(prop => delete rest[prop])
  return rest
}

export function dateStringConvert(fromDateFormat: string, toDateFormat: string, text: string) {
  if (text && fromDateFormat && toDateFormat)
    return moment(text, fromDateFormat)
      .format(toDateFormat)
  else
    return text
}


const IGNORE_CLASS = ['ObserverComponent', 'Connect', 'Injector']

export function getClassName(instance: object) {
  let _this = instance;
  while (IGNORE_CLASS.indexOf(_this.constructor.name) > -1)
    _this = _this['__proto__'];
  return _this.constructor.name
}
