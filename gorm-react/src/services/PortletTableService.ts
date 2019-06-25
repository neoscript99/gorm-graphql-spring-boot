import DomainService from 'oo-graphql-service/lib/DomainService';
import { MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';

export default class PortletTableService extends DomainService<MobxDomainStore> {

  getData(id: string): Promise<string[]> {
    return this.domainGraphql.apolloClient.query<{ portletTableData: string[] }>({
      query: gql`query portletTableDataQuery{
                  portletTableData(portletTableId: "${id}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => data.data.portletTableData)
  }
}
