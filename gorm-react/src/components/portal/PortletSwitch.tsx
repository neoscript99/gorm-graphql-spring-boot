import React, { Component, ReactNode } from 'react';
import { Entity } from 'oo-graphql-service';
import PortletLink from './PortletLink';
import PortletTable from './PortletTable';
import { Card, Icon } from 'antd'
import PortletListView from './PortletListView';
import { PortletProps } from './Portlet';

const defaultPortalMap: PortletMap = { PortletLink, PortletTable, PortletListView }

export interface PortletMap {
  [key: string]: React.ComponentType<PortletProps>
}

export interface PortletSwitchProps extends PortletProps {
  portletMap: PortletMap
}


const PortletSwitch = (props: PortletSwitchProps) => {
  return <div>
    <a id={props.portlet.id} />
    <PortletMapping {...props} />
  </div>
}
export default PortletSwitch;


const PortletMapping = ({ portlet, portletMap }: PortletSwitchProps) => {
    const map = { ...defaultPortalMap, ...portletMap };
    if (map[portlet.type]) {
      const Portlet = map[portlet.type];
      return <Portlet portlet={portlet} />
    } else
      return <NotExists portlet={portlet} />
  }
;

const NotExists = ({ portlet }: PortletProps) =>
  <Card title={portlet.portletName}
        actions={[<Icon type="setting" />, <Icon type="edit" />,
          <Icon type="ellipsis" />]}>
    <Card.Meta
      avatar={<Icon type="close" style={{ color: 'red', fontSize: '2rem' }} />}
      title="控件不存在，请联系管理员"
      description={portlet.type}
    />
  </Card>
