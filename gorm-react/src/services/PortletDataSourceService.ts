import DomainService from 'oo-graphql-service/lib/DomainService';
import { MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';

export default class PortletDataSourceService extends DomainService<MobxDomainStore> {

  getData(dataSourceId: string): Promise<string[]> {
    return this.domainGraphql.apolloClient.query<{ getPortletData: string[] }>({
      query: gql`query getPortletDataQuery{
                  getPortletData(dataSourceId: "${dataSourceId}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => data.data.getPortletData)
  }
}
