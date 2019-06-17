import { domainGraphql, livebosServerService } from '../services';

import { graphqlVars } from '../services';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';

graphqlVars.token = 'gorm-dev-token'
describe('livebos tests', () => {
  it('test server', async () => {
    const livebosServer = await livebosServerService.findFirst({})
    expect(livebosServer)
      .toBeTruthy()

    const userInfo = await livebosServerService.getUserInfo(livebosServer.id as string, 'admin');
    console.log(userInfo);
    expect(userInfo.name)
      .toEqual('管理员')


    const notice = await livebosServerService.queryNotices(livebosServer.id as string, 'admin');
    console.log(notice);
    expect(notice.result)
      .toEqual(1)
  });


  it('livebos object query test', async () => {
    const livebosQueryService = new DomainService('livebosQuery', MobxDomainStore, domainGraphql);
    const livebosQuery = await livebosQueryService.findFirst({})
    console.log(livebosQuery);
    expect(livebosQuery)
      .toBeTruthy()
    const result = await livebosServerService.objectQuery(livebosQuery.id as string);
    console.log(result.data);
    expect(result.count)
      .toBeGreaterThan(0)
    expect(result.data)
      .toBeTruthy()
  })
})
