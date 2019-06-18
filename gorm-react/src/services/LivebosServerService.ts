import DomainService from 'oo-graphql-service/lib/DomainService';
import { MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';

//{"id":0,"loginId":"admin","name":"管理员","lastLogin":"2019-06-13 17:46:54","grade":0,"status":1,"orgId":0}
export interface LiveUserInfo {
  id: number
  loginId: string
  name: string
  lastLogin: string
  grade: number
  status: number
  orgId: number
}

//{"workflowCount":4,"result":1,"message":"成功"}
export interface LivebosNotice {
  workflowCount: number
  result: number
  message: string
}

export interface LivebosObject {
  //最终数据转化到这里
  data: any[]
  queryId: string
  hasMore: boolean
  count: number
  metaData: LivebosObjectMetaData
  result: number
  message: string
  records: any[][]
}

export interface LivebosObjectMetaData {
  colCount: number
  colInfo: {
    name: string
    label: string
    type: number
    length: number
    scale: number
  }[]
}

export default class LivebosServerService extends DomainService<MobxDomainStore> {

  getUserInfo(livebosServerId: string, userId: string): Promise<LiveUserInfo> {
    return this.domainGraphql.apolloClient.query<{ livebosGetUserInfo: string }>({
      query: gql`query livebosGetUserInfo{
                  livebosGetUserInfo(livebosServerId: "${livebosServerId}", userId: "${userId}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.livebosGetUserInfo))
  }

  queryNotices(livebosServerId: string, userId: string, type: string = '0'): Promise<LivebosNotice> {
    return this.domainGraphql.apolloClient.query<{ livebosQueryNotices: string }>({
      query: gql`query livebosQueryNotices{
                  livebosQueryNotices(livebosServerId: "${livebosServerId}", userId: "${userId}", type: "${type}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.livebosQueryNotices))
  }

  objectQuery(livebosQueryId: string): Promise<LivebosObject> {
    return this.domainGraphql.apolloClient.query<{ livebosObjectQuery: string }>({
      query: gql`query livebosObjectQuery{
                  livebosObjectQuery(livebosQueryId: "${livebosQueryId}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.livebosObjectQuery) as LivebosObject)
      .then(lb => {
        const { records, metaData: { colInfo } } = lb;
        if (records && records.length > 0) {
          lb.data = records.map(record =>
            record.reduce((acc, value, idx) => (acc[colInfo[idx].name] = value, acc), {}))
        }
        return lb;
      })
  }
}
