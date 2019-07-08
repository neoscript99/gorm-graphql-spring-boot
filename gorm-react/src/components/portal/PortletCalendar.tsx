import React from 'react';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { observer } from 'mobx-react';
import { Badge, Calendar, Card } from 'antd';
import urlTemplate from 'url-template'
import Portlet from './Portlet';
import moment, { Moment } from 'moment';
import 'moment/locale/zh-cn';
import { portletCalendarService } from '../../services';

moment.locale('zh-cn');

@observer
class PortletCalendar extends Portlet {

  render() {
    if (!(this.state && this.state.portlet && this.state.data))
      return null;

    const { portletName } = this.state.portlet;
    return (
      <Card title={portletName}>
        <div style={{ border: '1px solid #d9d9d9', borderRadius: 4 }}>
          <Calendar fullscreen={false} dateCellRender={this.cellRender.bind(this, 'day')}
                    monthCellRender={this.cellRender.bind(this, 'month')} onSelect={this.calSelect} />
        </div>
      </Card>
    )
  }

  cellRender(unit: 'day' | 'month', date: Moment) {
    const { portlet, data } = this.state
    const num = data.reduce((sum, item) => {
      return (date.isBetween(moment(item[portlet.beginTimeField], portlet.timeFormat)
        , moment(item[portlet.endTimeField], portlet.timeFormat), unit, '[]'))
        ? sum + 1
        : sum;
    }, 0)
    return (
      num > 0 && <Badge count={num} offset={(unit === 'day') ? [12, -42] : undefined} />
    );
  }


  get portletService(): DomainService<MobxDomainStore> | null {
    return portletCalendarService;
  }

  calSelect = (date?: Moment) => {
    const { dateLink, timeFormat } = this.state.portlet;
    const linkTemplate = urlTemplate.parse(dateLink)

    window.open(linkTemplate.expand(date && { date: date.format(timeFormat) }), '_blank')
  }
}

export default PortletCalendar;
