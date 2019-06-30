import React, { Component } from 'react'
import DomainService, { ListOptions } from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';
import { TableProps } from 'antd/lib/table';
import { Entity } from 'oo-graphql-service';
import { getClassName } from '../utils/myutils';

export interface EntityListState {
  tableProps?: TableProps<Entity>
}


abstract class EntityList<P = any, S extends EntityListState = EntityListState>
  extends Component<P, S> {
  tableProps: TableProps<Entity> = { loading: false, pagination: false }
  firstMount = true;
  uuid = new Date();

  abstract get domainService(): DomainService<MobxDomainStore>;

  query(): Promise<ListResult> {
    const p = this.domainService.listAll(this.queryParam)
    this.showLoading(p)
    return p
  }

  showLoading(promise: Promise<any>): void {
    this.tableProps.loading = true
    this.updateState()
    promise.finally(() => {
      this.tableProps.loading = false
      this.updateState()
    })
  }

  componentDidMount(): void {
    // todo firstMount功能还无法实现，目前react route的component每次都是新实例
    if (this.firstMount) {
      console.debug(`${this.className}.componentDidMount:${this.toString()}`);
      this.query()
      this.firstMount = false
    }
  }

  protected updateState(): void {
    this.setState({ tableProps: this.tableProps })
  }

  protected get queryParam(): ListOptions {
    return {
      criteria: {},
      orders: [['lastUpdated', 'desc',]]
    };
  }

  toString() {
    return this.uuid
  }

  get className() {
    return getClassName(this)
  }

}

export default EntityList;
