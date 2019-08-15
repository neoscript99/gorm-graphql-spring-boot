import React, { ReactNode } from 'react'
import { observer } from 'mobx-react';
import { portletService } from '../../services'
import {
  EntityPageList,
  MobxDomainStore,
  DomainService,
  commonColumns,
  EntityColumnProps
} from 'oo-graphql-service';

const columns: EntityColumnProps[] = [
  { title: '控件名称', dataIndex: 'portletName' },
  { title: '所属门户', dataIndex: 'portal.portalName' },
  { title: '控件类型', dataIndex: 'type' },
  commonColumns.lastUpdated
];


@observer
export  class PortletManage extends EntityPageList {

  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService (): DomainService<MobxDomainStore> {
    return portletService;
  }
}
