import React, { Component } from 'react';

class NavMenu extends Component {
  render() {
    return (
      <div className="lq-nav">
        <ul>
          <li className="active">首页</li>
          <li>行内应用</li>
          <li>省联社应用</li>
          <li>其他应用</li>
          <li>绩效考核</li>
        </ul>
      </div>
    );
  }
}

export default NavMenu;
