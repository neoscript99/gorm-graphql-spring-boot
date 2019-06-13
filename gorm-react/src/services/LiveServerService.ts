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
export interface LiveNotice {
  workflowCount: number
  result: number
  message: string
}

export interface LiveObject {
  //最终数据转化到这里
  data: any[]
  queryId: string
  hasMore: boolean
  count: number
  metaData: LiveObjectMetaData
  result: number
  message: string
  records: any[][]
}

export interface LiveObjectMetaData {
  colCount: number
  colInfo: {
    name: string
    label: string
    type: number
    length: number
    scale: number
  }[]
}

export default class LiveServerService extends DomainService<MobxDomainStore> {

  getUserInfo(liveServerId: string, userId: string): Promise<LiveUserInfo> {
    return this.domainGraphql.apolloClient.query<{ liveGetUserInfo: string }>({
      query: gql`query liveGetUserInfo{
                  liveGetUserInfo(liveServerId: "${liveServerId}", userId: "${userId}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.liveGetUserInfo))
  }

  queryNotices(liveServerId: string, userId: string, type: string = '0'): Promise<LiveNotice> {
    return this.domainGraphql.apolloClient.query<{ liveQueryNotices: string }>({
      query: gql`query liveQueryNotices{
                  liveQueryNotices(liveServerId: "${liveServerId}", userId: "${userId}", type: "${type}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.liveQueryNotices))
  }

  objectQuery(liveQueryId: string): Promise<LiveObject> {
    return this.domainGraphql.apolloClient.query<{ liveObjectQuery: string }>({
      query: gql`query liveObjectQuery{
                  liveObjectQuery(liveQueryId: "${liveQueryId}")
                }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => JSON.parse(data.data.liveObjectQuery) as LiveObject)
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
