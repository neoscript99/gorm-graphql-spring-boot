import React, { Component } from 'react';
import { Entity } from 'oo-graphql-service';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { livebosQueryService, livebosServerService, portletDsService } from '../../services';
import { LivebosObject } from '../../services/LivebosServerService';
import { ColumnProps } from 'antd/lib/table';

export interface PortletProps {
  //props中只有基类的基础信息，扩展信息还需单独获得
  portlet: Entity
  inTab?: boolean

  [key: string]: any
}

export interface PortletState {
  //state中包含扩展信息
  portlet: Entity
  columns: ColumnProps<Entity>[]
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
    this.setState({ portlet, columns: portlet.columns && JSON.parse(portlet.columns) })
    //根据数据源类型，获取对应数据，保存到state.data
    if (portlet.ds && portlet.ds.type === 'LivebosQuery')
      this.livebosObjectQuery(portlet)
    else if (portlet.ds && portlet.ds.type === 'RdbQuery')
      this.rdbQuery(portlet)

  }

  async livebosObjectQuery(portlet: Entity) {
    portlet.ds = await livebosQueryService.get(portlet.ds.id)
    const livebosObject: LivebosObject = await livebosServerService.objectQuery(portlet.ds.id)
    console.debug('livebosObjectQuery: ', livebosObject);
    this.setState({ data: livebosObject.data })
  }

  async rdbQuery(portlet: Entity) {
    const jsonList = await portletDsService.getData(portlet.ds.id)
    this.setState({ data: jsonList.map(json => JSON.parse(json)) })
  }
}

export default Portlet;
