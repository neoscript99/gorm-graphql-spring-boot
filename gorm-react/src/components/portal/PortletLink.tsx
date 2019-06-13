import React, { ReactNode } from 'react';
import { Entity } from 'oo-graphql-service';
import { portletLinkService } from '../../services';
import { observer } from 'mobx-react';
import { Button } from 'antd';

@observer
class PortletLink extends React.Component<{ portlet: Entity }, { link: Entity }> {

  componentDidMount() {
    portletLinkService.get(this.props.portlet.id)
      .then(link => this.setState({ link }))
  }

  render(): ReactNode {
    if (this.state && this.state.link) {
      const { link } = this.state
      return <Button type="primary" icon="link" size='large' style={{ height: '6rem' }}
                     onClick={() => window.open(link.linkUrl, '_blank')}>
        {link.portletName}
      </Button>
    }
    else
      return null
  }
}

export default PortletLink;
