import React, { Component } from 'react';
import { Avatar, Badge } from 'antd';
import logo from '../../asset/img/logo.png';
import hIcon1 from '../../asset/img/h-icon1.png';
import hIcon2 from '../../asset/img/h-icon2.png';
import hIcon3 from '../../asset/img/h-icon3.png';
import { livebosServerService } from '../../services';

class HeaderIcons extends Component<any, any> {
  state = { server: { serverRoot: '' }, noticeInfo: { workflowCount: 0 } }

  async componentDidMount() {
    const server = await livebosServerService.findFirst({ eq: [['serverName', 'LuqiaoServer']] })
    server && livebosServerService.queryNotices(server.id as string, 'admin')
      .then(noticeInfo => this.setState({ server, noticeInfo }))
  }

  render() {
    const { server, noticeInfo: { workflowCount } } = this.state
    return (
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <img src={logo} style={{ margin: '0.5rem', width: '315px', height: '67px' }} />
        <div className='flex-row'>
          <Badge count={workflowCount} offset={[-10, 5]}>
            <a href={server.serverRoot + '/UIProcessor?Table=BPM_Workflow_Center'} target="_blank">
              <div className='head-icon'>
                <img src={hIcon1} />
                <span>待办</span>
              </div>
            </a>
          </Badge>
          <a href={server.serverRoot + '/plugin/msg.do?msgCode=Workflow'} target="_blank">
            <div className='head-icon'>
              <img src={hIcon2} />
              <span>消息</span>
            </div>
          </a>
          <div className='head-icon'><img src={hIcon3} /><span>预警</span></div>
          <div className='flex-row' style={{ alignItems: 'center', margin: '0 1rem' }}>
            <Avatar icon="user" style={{ backgroundColor: '#f56a00' }} />
            <a style={{ margin: '0 0.2rem' }}>张希希</a>
          </div>
        </div>
      </div>
    );
  }
}

export default HeaderIcons;
