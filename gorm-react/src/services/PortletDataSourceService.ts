import DomainService from 'oo-graphql-service/lib/DomainService';
import { Entity, MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';
import { LivebosObject, transLivebosData } from './LivebosServerService';

export interface DataResult {
  data: any[] | any
  [key: string]: any
}

export default class PortletDataSourceService extends DomainService<MobxDomainStore> {

  getData(portletDataSource: Entity): Promise<DataResult> {
    return this.domainGraphql.apolloClient.query<{ getPortletData: string }>({
      query: gql`query getPortletDataQuery{
                  getPortletData(dataSourceId: "${portletDataSource.id}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => {
        const jsonData = JSON.parse(data.data.getPortletData);
        if (portletDataSource.type === 'LivebosQuery')
          return transLivebosData(jsonData as LivebosObject)
        return { data: jsonData }
      })
  }
}
