import { sha256 } from 'js-sha256'
import { dateStringConvert } from '../utils/myutils';

it('sha256 test', () => {
  expect(sha256('anonymous'))
    .toEqual('2f183a4e64493af3f377f745eda502363cd3e7ef6e4d266d444758de0a85fcc8')
})


it('date string convert test', () => {
  const date1 = '2019-06-14 08:53:04'
  const date2 = '2019-06-14 18:53:04'
  const item = { fromDateFormat: 'YYYY-MM-DD HH:mm:ss', toDateFormat: 'YYYY-MM-DD' }
  expect(dateStringConvert(date1, item))
    .toEqual('2019-06-14')
  expect(dateStringConvert(date2, item))
    .toEqual('2019-06-14')
})

