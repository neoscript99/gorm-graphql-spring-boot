import React, { Component, ReactNode } from 'react'
import DomainService, { ListOptions } from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { ListResult } from 'oo-graphql-service/lib/DomainGraphql';
import { TableProps } from 'antd/lib/table';
import { Entity } from 'oo-graphql-service';
import { PaginationConfig } from 'antd/lib/pagination';

export interface EntityListState {
  tableProps: {
    loading: boolean
    pagination: PaginationConfig | false;
  }
}


abstract class EntityList<P =any, S extends EntityListState= EntityListState>
  extends Component<P, S> {
  constructor(props: P, initState: S) {
    super(props)
    initState.tableProps = { loading: false, pagination: false }
    this.state = initState
  }

  abstract get domainService(): DomainService<MobxDomainStore>;

  query(): Promise<ListResult> {
    this.setState({ tableProps: { ...this.state.tableProps, loading: true } })
    return this.domainService.listAll(this.queryParam)
      .finally(() =>
        this.setState({ tableProps: { ...this.state.tableProps, loading: false } })
      )
  }

  componentDidMount(): void {
    this.query()
  }

  protected get queryParam(): ListOptions {
    return {};
  }
}

export default EntityList;
