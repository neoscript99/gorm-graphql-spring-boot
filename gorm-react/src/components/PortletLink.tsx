import React, { PureComponent } from 'react';
import { Entity } from 'oo-graphql-service';
import { portletLinkService } from '../services';
import { observer } from 'mobx-react';

@observer
class PortletLink extends PureComponent<{ portlet: Entity }, { link: Entity }> {
  state = { link: { id: '', linkUrl: '', imageUrl: '', portletName: '' } }

  componentDidMount() {
    portletLinkService.get(this.props.portlet.id)
      .then(value => this.setState({ link: value }))
  }

  render() {
    const { link } = this.state
    return (
      link && <div>
        <a href={link.linkUrl} target='_blank'>{link.imageUrl ?
          <img src={link.imageUrl} width='100%' /> : link.portletName}</a>
      </div>
    );
  }
}

export default PortletLink;
