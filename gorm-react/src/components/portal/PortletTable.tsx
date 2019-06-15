import React from 'react';
import { Entity } from 'oo-graphql-service';
import { portletDsService, portletTableService } from '../../services';
import { observer } from 'mobx-react';
import { Card, Table } from 'antd';

interface S {
  table: Entity
  columns: any[]
  tableData: any[]
}

@observer
class PortletTable extends React.Component<{ portlet: Entity, ds: any }, S> {
  componentDidMount() {
    const { portlet, ds } = this.props;
    portletTableService.get(portlet.id)
      .then(table => this.setState({ table, columns: JSON.parse(table.columns) }))
    portlet.id && portletDsService.getData(ds.id)
      .then(jsonList => this.setState({ tableData: jsonList.map(json => JSON.parse(json)) }))
  }

  render() {
    if (this.state && this.state.columns && this.state.tableData) {
      const { table, columns, tableData } = this.state
      return <Card title={table.portletName}>
        <Table dataSource={tableData} columns={columns}
               rowKey={table.rowKey}
               pagination={{ pageSize: 5 }} bordered />
      </Card>
    } else
      return null
  }
}

export default PortletTable;