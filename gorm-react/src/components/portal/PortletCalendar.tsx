import React from 'react';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { observer } from 'mobx-react';
import { Badge, Calendar, Card, Popover, List, Icon, Divider } from 'antd';
import urlTemplate from 'url-template'
import Portlet from './Portlet';
import moment, { Moment } from 'moment';
import 'moment/locale/zh-cn';
import { portletCalendarService } from '../../services';

moment.locale('zh-cn');

@observer
class PortletCalendar extends Portlet {

  render() {
    if (!(this.state && this.state.portlet))
      return null;

    const { portletName, dateLink } = this.state.portlet;
    return (
      <Card title={portletName} extra={<a href={dateLink} target='_blank'>更多</a>}>
        <div style={{ border: '1px solid #d9d9d9', borderRadius: 4 }}>
          <Calendar fullscreen={false} dateCellRender={this.cellRender.bind(this, 'day')}
                    monthCellRender={this.cellRender.bind(this, 'month')} />
        </div>
      </Card>
    )
  }

  cellRender(unit: 'day' | 'month', date: Moment) {
    const { portlet, dataList } = this.state
    if (!dataList)
      return null;

    const matchData = dataList.filter((item) => date.isBetween(moment(item[portlet.beginTimeField], portlet.timeFormat)
      , moment(item[portlet.endTimeField], portlet.timeFormat), unit, '[]'))

    const linkTemplate = urlTemplate.parse(portlet.titleLink)
    return (
      <Popover placement="bottom" content={<List dataSource={matchData} renderItem={item => (
        <List.Item>
          <Icon type="calendar" /><Divider type="vertical" />
          <a href={linkTemplate.expand(item)} target="_blank">{item[portlet.titleField]}</a>
        </List.Item>
      )} />}>
        <Badge count={matchData.length} />
      </Popover>
    );
  }


  get portletService(): DomainService<MobxDomainStore> | null {
    return portletCalendarService;
  }

  /**
   * 加了onSelect={this.calSelect}后，点击popup中的链接后打开两个链接，应该是bug，先放到标题栏
   * @param date
   */
  calSelect = (date?: Moment) => {
    const { dateLink, timeFormat } = this.state.portlet;
    const linkTemplate = urlTemplate.parse(dateLink)

    window.open(linkTemplate.expand(date && { date: date.format(timeFormat) }), '_blank')
  }
}

export default PortletCalendar;
