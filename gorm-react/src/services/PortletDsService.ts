import DomainService from 'oo-graphql-service/lib/DomainService';
import { MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';

export default class PortletDsService extends DomainService<MobxDomainStore> {

  getData(id: string): Promise<string[]> {
    return this.domainGraphql.apolloClient.query<{ portletData: string[] }>({
      query: gql`query portletDataQuery{
                  portletData(dsId: "${id}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => data.data.portletData)
  }
}
