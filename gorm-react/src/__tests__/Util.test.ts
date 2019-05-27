import { sha256 } from 'js-sha256'

it('sha256 test', () => {
  expect(sha256('anonymous'))
    .toEqual('2f183a4e64493af3f377f745eda502363cd3e7ef6e4d266d444758de0a85fcc8')
})
