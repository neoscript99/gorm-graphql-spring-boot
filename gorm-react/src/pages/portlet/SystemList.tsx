import React, { Component } from 'react';
import Portlet from '../../components/portal/Portlet';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import icon1 from '../asset/img/icon1.png'
import icon2 from '../asset/img/icon2.png'
import icon3 from '../asset/img/icon3.png'
import icon4 from '../asset/img/icon4.png'
import icon5 from '../asset/img/icon5.png'
import icon6 from '../asset/img/icon6.png'
import icon7 from '../asset/img/icon7.png'

const iconMap = { icon1, icon2, icon3, icon4, icon5, icon6, icon7 }

const dataSource = [{ name: '档案系统', icon: 'icon1' }, { name: '老OA系统', icon: 'icon2' }, { name: 'HR系统', icon: 'icon3' },
  { name: '员工管理', icon: 'icon4' }, { name: '绩效系统', icon: 'icon5' }, { name: '营销系统', icon: 'icon6' }, {
    name: 'HA系统',
    icon: 'icon7'
  }]

class SystemList extends Portlet {
  get portletService(): DomainService<MobxDomainStore> | null {
    return null;
  }

  render() {
    return (
      <div>

      </div>
    );
  }
}

export default SystemList;
