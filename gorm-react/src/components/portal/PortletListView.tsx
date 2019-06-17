import React from 'react';
import Portlet from './Portlet';
import { DomainService, Entity, MobxDomainStore } from 'oo-graphql-service';
import { portletListViewService } from '../../services';
import { Card, Table } from 'antd';
import { dateStringConvert } from '../../utils/myutils';
import { ColumnProps } from 'antd/lib/table';


class PortletListView extends Portlet {
  render() {
    if (!this.state)
      return null;

    const {
      portlet: {
        portletName, titleFields, cateField, dateField, extraLink, fromDateFormat, toDateFormat,
        ds: { objectName, livebosServer: { serverRoot } }
      },
      livebosObject: { data }
    } = this.state
    const columns: ColumnProps<Entity>[] = [
      { title: 'category', dataIndex: cateField, render: (text: string) => `[${text}]` },
      {
        title: 'title',
        key: 'titleFields',
        render: (text: string, record: any) =>
          (titleFields as string).split(',')
            .map(tt => record[tt])
            .join('-')
      },
      {
        title: 'date',
        dataIndex: dateField,
        align: 'right',
        render: dateStringConvert.bind(null, fromDateFormat, toDateFormat)
      }
    ]
    return <Card title={portletName}
                 extra={<a href={extraLink || `${serverRoot}/UIProcessor?Table=${objectName}`} target='_blank'>更多</a>}>
      <Table dataSource={data} columns={columns}
             rowKey='ID' pagination={false} showHeader={false} size='middle' bordered={false} />
    </Card>
  }

  get portletService(): DomainService<MobxDomainStore> {
    return portletListViewService;
  }
}

export default PortletListView;
