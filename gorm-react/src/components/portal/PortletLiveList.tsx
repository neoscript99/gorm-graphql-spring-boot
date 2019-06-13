import React from 'react';
import Portlet from './Portlet';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { portletLiveListService } from '../../services';
import { Card, List, Typography } from 'antd';

const columns = [
  { 'title': '用户姓名', 'dataIndex': 'Name' },
  { 'title': '用户类别', 'dataIndex': 'Grade' },
  { 'title': '最近登录时间', 'dataIndex': 'LastLogin' }
]

class PortletLiveList extends Portlet {
  render() {
    if (!this.state)
      return null;

    const { portlet: { portletName, titleField, cateField, dateField }, liveObject: { data } } = this.state
    return <Card title={portletName}>
      <List
        dataSource={data}
        renderItem={item => (
          <List.Item key={item.ID}>
            <Typography.Text mark>[{item[cateField]}]</Typography.Text>
            <List.Item.Meta
              title={item[titleField]}
              description={item.UserID}
            />
            <div>{item[dateField]}</div>
          </List.Item>
        )}
      />
    </Card>
  }

  get portletService(): DomainService<MobxDomainStore> {
    return portletLiveListService;
  }
}

export default PortletLiveList;
