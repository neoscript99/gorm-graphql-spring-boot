import React from 'react';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { observer } from 'mobx-react';
import { Badge, Calendar, Card } from 'antd';
import Portlet from './Portlet';
import moment, { Moment } from 'moment';
import 'moment/locale/zh-cn';

moment.locale('zh-cn');

@observer
class PortletCalendar extends Portlet {

  render() {
    if (!(this.state && this.state.portlet))
      return null

    const { portlet } = this.state
    return (
      <Card title={portlet.portletName}>
        <div style={{ border: '1px solid #d9d9d9', borderRadius: 4 }}>
          <Calendar fullscreen={false} dateCellRender={this.dateCellRender} monthCellRender={this.monthCellRender} />
        </div>
      </Card>
    )
  }

  dateCellRender(value: Moment) {
    return (
      value.date() == 11 && <Badge count={3} offset={[12, -42]} />
    );
  }

  monthCellRender(value: Moment) {
    return (
      value.month() == 6 && <Badge count={7} />
    );
  }

  get portletService(): DomainService<MobxDomainStore> | null {
    return null;
  }
}

export default PortletCalendar;
