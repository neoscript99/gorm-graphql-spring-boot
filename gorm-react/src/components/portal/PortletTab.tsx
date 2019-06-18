import React from 'react';
import { DomainService, MobxDomainStore } from 'oo-graphql-service';
import { portletTabRelService } from '../../services';
import { observer } from 'mobx-react';
import { Card, Tabs } from 'antd';
import Portlet from './Portlet';
import PortletSwitch from './PortletSwitch';


const { TabPane } = Tabs;

@observer
class PortletTab extends Portlet {

  async componentDidMount() {
    const res = await portletTabRelService.listAll({
      criteria: { eq: [['tab.id', this.props.portlet.id]] },
      orders: ['portletOrder']
    })
    this.setState({ portletList: res.results.map(rel => rel.portlet) })
  }

  render() {
    if (!(this.state && this.state.portletList))
      return null

    const { portletList } = this.state
    return (
      <Card>
        <Tabs>
          {portletList.map(ptl => <TabPane tab={ptl.portletName} key={ptl.id}>
            <PortletSwitch key={ptl.id} portlet={ptl} inTab={true} />
          </TabPane>)}
        </Tabs>
      </Card>
    )
  }

  get portletService(): DomainService<MobxDomainStore> | null {
    return null;
  }
}

export default PortletTab;
