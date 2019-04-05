import { GraphqlClient } from 'oo-graphql-mobx'
import gql from 'graphql-tag'
import { observable } from 'mobx';

export type MenuNode = {
  menu: any;
  subMenus: Array<MenuNode>
}

class MenuStore {
  @observable
  menuTree: MenuNode = { menu: {}, subMenus: new Array<MenuNode>() };

  constructor(private graphqlClient: GraphqlClient) {

  }

  getMenuTree(token: String) {
    this.graphqlClient.getFields('MenuNode')
      .then(fields => this.graphqlClient.client.query<{ [key: string]: MenuNode }>({
        query: gql`query menuTree {
                      menuTree(token: "${token}") {
                      ${fields}
                    }}`,
        fetchPolicy: 'no-cache',
        variables: {
          ...this.graphqlClient.defaultVariables
        }
      }))
      .then(result => this.menuTree = result.data.menuTree)
  }
}

export default MenuStore
