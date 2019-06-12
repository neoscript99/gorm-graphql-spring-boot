import React, { Component } from 'react';
import { Avatar, Badge, Layout, Menu } from 'antd';

import './LuqiaoPortal.css'
import { observer } from 'mobx-react'
import logo from '../asset/img/logo.png'
import hIcon1 from '../asset/img/h-icon1.png'
import hIcon2 from '../asset/img/h-icon2.png'
import hIcon3 from '../asset/img/h-icon3.png'
import inform from '../asset/img/inform.png'
import mail from '../asset/img/mail.png'
import icon1 from '../asset/img/icon1.png'
import icon2 from '../asset/img/icon2.png'
import icon3 from '../asset/img/icon3.png'
import icon4 from '../asset/img/icon4.png'
import icon5 from '../asset/img/icon5.png'
import icon6 from '../asset/img/icon6.png'
import icon7 from '../asset/img/icon7.png'

const {
  Header, Content
} = Layout

@observer
class LuqiaoPortal extends Component {
  render() {
    return (
      <Layout id='luqiao'>
        <Header
          style={{ background: '#fff', padding: 0, height: 'auto' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <img src={logo} style={{ margin: '0.5rem', width: '315px', height: '67px' }} />
            <div className='flex-row'>
              <div className='head-icon'>
                <img src={hIcon1} />
                <span>待办</span>
              </div>
              <Badge count={5} offset={[-10, 5]}>
                <div className='head-icon'>
                  <img src={hIcon2} />
                  <span>消息</span>
                </div>
              </Badge>
              <div className='head-icon'><img src={hIcon3} /><span>预警</span></div>
              <div className='flex-row' style={{ alignItems: 'center', margin: '0 1rem' }}>
                <Avatar icon="user" style={{ backgroundColor: '#f56a00' }} />
                <a style={{ margin: '0 0.2rem' }}>张希希</a>
              </div>
            </div>
          </div>
          <div className="lq-nav">
            <ul>
              <li className="active">首页</li>
              <li>行内应用</li>
              <li>省联社应用</li>
              <li>其他应用</li>
              <li>绩效考核</li>
            </ul>
          </div>
          <div className="flex-row"
               style={{ justifyContent: 'space-between', height: '36px', lineHeight: '36px', fontSize: '12px' }}>
            <div className="flex-row" style={{ alignItems: 'center', margin: '0 1rem' }}>
              <img src={inform} />
              <span style={{ margin: '0 0.5rem', color: '#028CCC' }}>系统更新小贴士</span>
              <span style={{ margin: '0 1rem' }}>1、系统升级通知</span>
              <span>2、通知公告</span>
            </div>
            <span style={{ color: '#999', margin: '0 1rem' }}>更多>></span>
          </div>
        </Header>
        <Content style={{ background: '#f4f4f4' }}>
          Content
        </Content>
      </Layout>
    );
  }
}

export default LuqiaoPortal;
