import { domainGraphql, liveServerService } from '../services';

import { graphqlVars } from '../services';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';

graphqlVars.token = 'gorm-dev-token'

it('test server', async () => {
  const liveServer = await liveServerService.findFirst({})
  expect(liveServer)
    .toBeTruthy()

  const userInfo = await liveServerService.getUserInfo(liveServer.id as string, 'admin');
  console.log(userInfo);
  expect(userInfo.name)
    .toEqual('管理员')


  const notice = await liveServerService.queryNotices(liveServer.id as string, 'admin');
  console.log(notice);
  expect(notice.result)
    .toEqual(1)
});


it('livebos object query test', async () => {
  const liveQueryService = new DomainService('liveQuery', MobxDomainStore, domainGraphql);
  const liveQuery = await liveQueryService.findFirst({})
  console.log(liveQuery);
  expect(liveQuery).toBeTruthy()
  const result = await liveServerService.objectQuery(liveQuery.id as string);
  console.log(result.data);
  expect(result.count)
    .toBeGreaterThan(0)
  expect(result.data).toBeTruthy()
})
