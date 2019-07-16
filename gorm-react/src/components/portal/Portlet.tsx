import React, { Component } from 'react';
import { Entity } from 'oo-graphql-service';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { portletDataSourceService } from '../../services';

export interface PortletProps {
  //props中只有基类的基础信息，扩展信息还需单独获得
  portlet: Entity
  inTab?: boolean

  [key: string]: any
}

export interface PortletState {
  //state中包含扩展信息
  portlet: Entity
  //列表结果
  dataList?: any[]
  //单个对象结果
  dataItem?: any

  [key: string]: any
}

abstract class Portlet<P extends PortletProps = PortletProps, S extends PortletState = PortletState> extends Component<P, S> {

  abstract get portletService(): DomainService<MobxDomainStore> | null ;

  componentDidMount() {
    /**
     * state.portlet是实现类包含所有信息，props.portlet只有基类信息
     * @see neo.script.gorm.portal.domain.pt.plet.Portlet
     */
    if (this.portletService)
      this.portletService.get(this.props.portlet.id)
        .then(portlet => this.setState({ portlet }))

    this.props.portlet.ds && portletDataSourceService.getData(this.props.portlet.ds)
      .then(result =>
        this.setState({
          dataList: result && result.dataList
        }))
  }

}

export default Portlet;
