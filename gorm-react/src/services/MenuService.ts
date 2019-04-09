import { DomainGraphql } from 'oo-graphql-service'

import gql from 'graphql-tag';
import MenuStore, { MenuNode } from '../stores/MenuStore';

export default class MenuService {

  constructor(public store: MenuStore, private domainGraphql: DomainGraphql, private defaultVariables: any) {
  }

  getMenuTree(token: String) {
    this.domainGraphql.getFields('MenuNode')
      .then(fields => this.domainGraphql.apolloClient.query<{ [key: string]: MenuNode }>({
        query: gql`query menuTree {
                      menuTree(token: "${token}") {
                      ${fields}
                    }}`,
        fetchPolicy: 'no-cache',
        variables: {
          ...this.defaultVariables
        }
      }))
      .then(result => this.store.menuTree = result.data.menuTree)
  }
}
