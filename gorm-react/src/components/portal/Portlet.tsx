import React, { Component } from 'react';
import { Entity } from 'oo-graphql-service';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { portletDsService } from '../../services';

export interface PortletProps {
  //props中只有基类的基础信息，扩展信息还需单独获得
  portlet: Entity
  inTab?: boolean
  [key: string]: any
}

export interface PortletState {
  //state中包含扩展信息
  portlet: Entity
  data: any
  [key: string]: any
}

abstract class Portlet<P extends PortletProps = PortletProps, S extends PortletState = PortletState> extends Component<P, S> {

  abstract get portletService(): DomainService<MobxDomainStore> | null ;

  async componentDidMount() {
    const portlet = this.portletService
      ? await this.portletService.get(this.props.portlet.id)
      : this.props.portlet

    /**
     * state.portlet是实现类包含所有信息，props.portlet只有基类信息
     * @see neo.script.gorm.portal.domain.pt.plet.Portlet
     */
    const result = portlet.ds && (await portletDsService.getData(portlet.ds))
    this.setState({
      portlet,
      data: result && result.data
    })
  }

}

export default Portlet;
