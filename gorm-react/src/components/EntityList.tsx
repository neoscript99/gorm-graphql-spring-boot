import React, { Component } from 'react'
import DomainService, { ListOptions } from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';
import { TableProps } from 'antd/lib/table';
import { Entity } from 'oo-graphql-service';

export interface EntityListState {
  tableProps?: TableProps<Entity>
}


abstract class EntityList<P =any, S extends EntityListState= EntityListState>
  extends Component<P, S> {
  tableProps: TableProps<Entity> = { loading: false, pagination: false }

  abstract get domainService(): DomainService<MobxDomainStore>;

  query(): Promise<ListResult> {
    return this.showLoading<ListResult>(this.domainService.listAll(this.queryParam))
  }

  showLoading<T>(promise: Promise<T>): Promise<T> {
    this.tableProps.loading = true
    this.updateState()
    return promise.finally(() => {
      this.tableProps.loading = false
      this.updateState()
    })
  }

  componentDidMount(): void {
    this.query()
  }

  protected updateState(): void {
    this.setState({ tableProps: this.tableProps })
  }

  protected get queryParam(): ListOptions {
    return {};
  }
}

export default EntityList;
