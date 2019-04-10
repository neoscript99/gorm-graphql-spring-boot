import { DomainGraphql } from 'oo-graphql-service'

import gql from 'graphql-tag';
import MenuStore, { MenuNode } from '../stores/MenuStore';

export default class MenuService {
  menuDodeFields: Promise<string>;

  constructor(public store: MenuStore, private domainGraphql: DomainGraphql, private defaultVariables: any) {
    this.menuDodeFields = domainGraphql.getFields('MenuNode')
  }

  getMenuTree(token: String) {
    this.menuDodeFields.then(fields =>
      this.domainGraphql.apolloClient.query<{ [key: string]: MenuNode }>({
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
