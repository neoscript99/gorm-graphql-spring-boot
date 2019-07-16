import { DomainGraphql, DomainService } from 'oo-graphql-service'

import gql from 'graphql-tag';
import MenuStore, { MenuNode } from '../stores/MenuStore';

export default class MenuService extends DomainService<MenuStore> {
  menuNodeFields: Promise<string>;

  constructor(domainGraphql: DomainGraphql) {
    super('menu', MenuStore, domainGraphql);
    this.menuNodeFields = domainGraphql.getFields('MenuNode')
  }

  getMenuTree(token: String): void {
    this.menuNodeFields.then(fields =>
      this.domainGraphql.apolloClient.query<{ [key: string]: MenuNode }>({
        query: gql`query menuTreeQuery {
                      menuTree {
                      ${fields}
                    }}`,
        fetchPolicy: 'no-cache',
        variables: {
          ...this.domainGraphql.defaultVariables
        }
      }))
      .then(result => this.store.menuTree = result.data.menuTree)
  }
}
