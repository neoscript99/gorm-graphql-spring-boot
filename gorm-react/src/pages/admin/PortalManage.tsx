import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { portalService } from '../../services'
import {
  EntityPageList,
  MobxDomainStore,
  DomainService,
  booleanLabel,
  timeFormater,
  EntityColumnProps
} from 'oo-graphql-service';

const { store } = portalService
const columns: EntityColumnProps[] = [
  { title: '门户名称', dataIndex: 'portalName' },
  { title: '门户图标', dataIndex: 'portalIcon' },
  { title: '是否启用', dataIndex: 'enabled', render: booleanLabel },
  { title: '修改时间', dataIndex: 'lastUpdated', render: timeFormater }];


@observer
export class PortalManage extends EntityPageList {
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService(): DomainService<MobxDomainStore> {
    return portalService;
  }
}
