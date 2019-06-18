import React from 'react';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { Card, Divider } from 'antd';
import icon1 from '../../../asset/img/icon1.png'
import icon2 from '../../../asset/img/icon2.png'
import icon3 from '../../../asset/img/icon3.png'
import icon4 from '../../../asset/img/icon4.png'
import icon5 from '../../../asset/img/icon5.png'
import icon6 from '../../../asset/img/icon6.png'
import addIcon from '../../../asset/img/icon7.png'
import Portlet from '../../../components/portal/Portlet';

const iconMap: { [key: string]: string } = { icon1, icon2, icon3, icon4, icon5, icon6, addIcon }

const dataSource: { name: string, icon: string }[] = [
  { name: '档案系统', icon: 'icon1' },
  { name: '老OA系统', icon: 'icon2' },
  { name: 'HR系统', icon: 'icon3' },
  { name: '员工管理', icon: 'icon4' },
  { name: '绩效系统', icon: 'icon5' },
  { name: '营销系统', icon: 'icon6' },
  { name: 'HA系统', icon: 'icon3' }]

class SystemList extends Portlet {
  get portletService(): DomainService<MobxDomainStore> | null {
    return null;
  }

  render() {
    if (!this.state)
      return null;

    const {
      portlet: { portletName, extraLink },
      data
    } = this.state
    return (
      <Card title={portletName}
            extra={extraLink && <a href={extraLink} target='_blank'>全部系统</a>}>
        <div className='flex-row'>
          {
            dataSource.map(item => <SystemDisplay key={item.name} {...item} />)
          }
          <SystemDisplay name='添加系统' icon='addIcon' />
        </div>
        <Divider>常用系统</Divider>
        <div className='flex-row'>
          {
            dataSource.map(item => <SystemDisplay key={item.name} {...item} />)
          }
        </div>
      </Card>
    );
  }
}

const SystemDisplay = (props: any) => <div className='flex-col' style={{ alignItems: 'center' }}>
  <img src={iconMap[props.icon]} style={{ margin: '0.5rem' }} />
  <span>{props.name}</span>
</div>
export default SystemList;
