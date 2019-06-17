import React, { Component } from 'react';
import { Entity } from 'oo-graphql-service';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { livebosQueryService, livebosServerService } from '../../services';
import { LivebosObject } from '../../services/LivebosServerService';

export interface PortletProps {
  //props中只有基类的基础信息，扩展信息还需单独获得
  portlet: Entity

  [key: string]: any
}

export interface PortletState {
  //state中包含扩展信息
  portlet: Entity
  livebosObject: LivebosObject

  [key: string]: any
}

abstract class Portlet<P extends PortletProps = PortletProps, S extends PortletState = PortletState> extends Component<P, S> {

  abstract get portletService(): DomainService<MobxDomainStore>;

  async componentDidMount() {
    const portlet = await this.portletService.get(this.props.portlet.id)

    if (portlet.ds && portlet.ds.type === 'LivebosQuery') {
      //子类信息，需要单独获取
      portlet.ds = await livebosQueryService.get(portlet.ds.id)
      const livebosObject: LivebosObject = await livebosServerService.objectQuery(portlet.ds.id)
      this.setState({ portlet, livebosObject })
    }
    this.setState({ portlet })
  }
}

export default Portlet;
