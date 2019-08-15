import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { rdbQueryService } from '../../services'
import {
  EntityColumnProps,
  EntityPageList,
  MobxDomainStore,
  DomainService,
  commonColumns,
} from 'oo-graphql-service';

const { store } = rdbQueryService
const columns: EntityColumnProps[] = [
  { title: '数据库名称', dataIndex: 'db.dbName' },
  { title: '查询名称', dataIndex: 'queryName' },
  { title: 'Sql', dataIndex: 'sql' },
  commonColumns.lastUpdated];


@observer
export class PortalDbQueryManage extends EntityPageList {

  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService(): DomainService<MobxDomainStore> {
    return rdbQueryService;
  }
}
