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

  abstract get portletService(): DomainService<MobxDomainStore> | null ;

  async componentDidMount() {
    const portlet = this.portletService
      ? await this.portletService.get(this.props.portlet.id)
      : this.props.portlet

    if (portlet.ds && portlet.ds.type === 'LivebosQuery') {
      //子类信息，需要单独获取
      this.livebosObjectQuery(portlet)
    } else
      this.setState({ portlet })
  }

  async livebosObjectQuery(portlet: Entity) {
    portlet.ds = await livebosQueryService.get(portlet.ds.id)
    const livebosObject: LivebosObject = await livebosServerService.objectQuery(portlet.ds.id)
    console.debug('livebosObjectQuery: ', livebosObject);
    this.setState({ portlet, livebosObject })
  }
}

export default Portlet;
