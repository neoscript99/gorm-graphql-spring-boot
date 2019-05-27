import React from 'react'
import { Redirect } from 'react-router';
import { userService } from '../services';

export default () => {
  userService.clearLoginInfoLocal()
  userService.changeCurrentItem({})
  return <Redirect to="/login/" />
}
