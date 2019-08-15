import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { Table } from 'antd';
import { rdbServerService } from '../../services'
import { EntityColumnProps, DomainService, MobxDomainStore, EntityPageList, commonColumns } from 'oo-graphql-service';

const { store } = rdbServerService
const columns: EntityColumnProps[] = [
  { title: '数据库名称', dataIndex: 'dbName' },
  { title: '驱动类', dataIndex: 'driverClassName' },
  { title: '连接url', dataIndex: 'url' },
  { title: '用户名', dataIndex: 'username' },
  { title: '密码', dataIndex: 'password' },
  { title: '测试Sql', dataIndex: 'testSql' },
  commonColumns.lastUpdated];


@observer
export class PortalDbManage extends EntityPageList {

  get domainService(): DomainService<MobxDomainStore> {
    return rdbServerService;
  }

  get columns(): EntityColumnProps[] {
    return columns;
  }
}
