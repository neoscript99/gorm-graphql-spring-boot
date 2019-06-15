import { domainGraphql, livebosServerService } from '../services';

import { graphqlVars } from '../services';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';

graphqlVars.token = 'gorm-dev-token'

it('test server', async () => {
  const liveServer = await livebosServerService.findFirst({})
  expect(liveServer)
    .toBeTruthy()

  const userInfo = await livebosServerService.getUserInfo(liveServer.id as string, 'admin');
  console.log(userInfo);
  expect(userInfo.name)
    .toEqual('管理员')


  const notice = await livebosServerService.queryNotices(liveServer.id as string, 'admin');
  console.log(notice);
  expect(notice.result)
    .toEqual(1)
});


it('livebos object query test', async () => {
  const liveQueryService = new DomainService('liveQuery', MobxDomainStore, domainGraphql);
  const liveQuery = await liveQueryService.findFirst({})
  console.log(liveQuery);
  expect(liveQuery).toBeTruthy()
  const result = await livebosServerService.objectQuery(liveQuery.id as string);
  console.log(result.data);
  expect(result.count)
    .toBeGreaterThan(0)
  expect(result.data).toBeTruthy()
})
