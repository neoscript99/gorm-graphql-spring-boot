import React, { Component } from 'react';
import { Entity } from 'oo-graphql-service';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { livebosServerService } from '../../services';
import { LiveObject } from '../../services/LiveServerService';

export interface PortletProps {
  //props中只有基类的基础信息，扩展信息还需单独获得
  portlet: Entity

  [key: string]: any
}

export interface PortletState {
  //state中包含扩展信息
  portlet: Entity
  liveObject: LiveObject

  [key: string]: any
}

abstract class Portlet<P extends PortletProps = PortletProps, S extends PortletState = PortletState> extends Component<P, S> {

  abstract get portletService(): DomainService<MobxDomainStore>;

  componentDidMount() {
    this.portletService.get(this.props.portlet.id)
      .then(portlet => {
        if (portlet.liveQuery) {
          livebosServerService.objectQuery(portlet.liveQuery.id)
            .then(liveObject => this.setState({ portlet, liveObject }))
        } else
          this.setState({ portlet })
      })
  }
}

export default Portlet;
