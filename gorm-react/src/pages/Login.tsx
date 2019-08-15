import React, { Component, FormEvent, ReactNode } from 'react'

import { Form, Icon, Input, Button, Checkbox } from 'antd'

import './Login.css'
import { userService } from '../services';
import { Redirect } from 'react-router';
import { observer } from 'mobx-react';
import { FormComponentProps } from 'antd/lib/form';


@observer
class LoginForm extends Component<FormComponentProps<any>> {
  handleSubmit(e: FormEvent) {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        userService.login(values.username, values.password, values.remember)
      }
    });
  };

  /**
   * @see https://codepen.io/skielbasa/pen/xVgNNY
   * @returns {React.ReactNode}
   */
  render(): ReactNode {
    const { getFieldDecorator } = this.props.form;
    const { currentItem, lastRoutePath } = userService.store
    if (currentItem.account) {
      return <Redirect to={lastRoutePath} />
    }

    return (<div className="login-page">
        <div className="form-box l-col-wrap">
          <div className="l-col form-box__content">
            <h1 className="form-box__title">系统介绍</h1>
            <div>
              <p>前端技术： <em>React Typescript Mobx Graphql</em></p>
              <p>后端技术： <em>SpringBoot Hibernate JPA Graphql</em></p>
            </div>
          </div>
          <div className="l-col form-box__form">
            <Form onSubmit={this.handleSubmit.bind(this)} style={{ maxWidth: '300px' }}>
              <Form.Item>
                {getFieldDecorator('username', {
                  rules: [{ required: true, message: '用户名不能为空!' }],
                })(
                  <Input
                    prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                    placeholder="用户名"
                    autoComplete='username'
                  />,
                )}
              </Form.Item>
              <Form.Item>
                {getFieldDecorator('password', {
                  rules: [{ required: true, message: '密码不能为空!' }],
                })(
                  <Input
                    prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                    type="password"
                    autoComplete='password'
                    placeholder="密码"
                  />,
                )}
              </Form.Item>
              <Form.Item>
                {getFieldDecorator('remember', {
                  valuePropName: 'checked',
                  initialValue: true,
                })(<Checkbox>自动登录</Checkbox>)}
                <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
                  Log in
                </Button>
              </Form.Item>
            </Form>
          </div>
        </div>
      </div>
    )
  }
}

export const Login = Form.create({ name: 'normal_login' })(LoginForm);


export const Logout = () => {
  userService.clearLoginInfoLocal()
  userService.changeCurrentItem({})
  return <Redirect to="/login/" />
}
