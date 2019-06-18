import React, { Component } from 'react';
import inform from '../../asset/img/inform.png';

class NoticeBar extends Component {
  render() {
    return (
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
    );
  }
}

export default NoticeBar;
