import React, { Component, ReactNode } from 'react'
import { inject, observer } from 'mobx-react';
import { Table } from 'antd';
import { DomainStore } from 'oo-graphql-service';
import { paramService } from '../services'

const columns = [{
  title: 'id',
  dataIndex: 'id',
  key: 'id',
}, {
  title: '参数名',
  dataIndex: 'name',
  key: 'name',
}, {
  title: '值',
  dataIndex: 'value',
  key: 'value',
}];


@inject('paramStore')
@observer
export default class Param extends Component<{ paramStore: DomainStore }> {

  componentDidMount(): void {
    paramService.listAll()
  }

  render(): ReactNode {
    return <Table dataSource={this.props.paramStore.allList} columns={columns} />
  }
}
