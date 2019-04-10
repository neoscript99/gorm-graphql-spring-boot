import { ReactNode } from 'react'
import moment from 'moment';

export const timeFormater =
  (date: Date): ReactNode =>
    moment(date)
      .format('YYYY-MM-DD hh:mm')
