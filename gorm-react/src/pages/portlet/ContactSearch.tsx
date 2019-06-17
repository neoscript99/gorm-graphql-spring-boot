import React from 'react';
import mail from '../../asset/img/mail.png';
import Portlet from '../../components/portal/Portlet';
import DomainService from 'oo-graphql-service/lib/DomainService';
import MobxDomainStore from 'oo-graphql-service/lib/mobx/MobxDomainStore';
import { AutoComplete } from 'antd';

const { Option } = AutoComplete;

const dataSource = ['Burns Bay Road', 'Downing Street', 'Wall Street'];

class ContactSearch extends Portlet {
  get portletService(): DomainService<MobxDomainStore> | null {
    return null;
  }

  render() {
    return (
      <div className="flex-row"
           style={{
             alignItems: 'center',
             height: '4rem',
             border: '1px solid #D5F2FF',
             background: '#F3FAFF',
             justifyContent: 'center'
           }}>
        <img src={mail} style={{ marginLeft: '0.5rem' }} />
        {this.state && <AutoComplete
          style={{ flex: '1 1 auto', margin: '0 0.5rem' }}
          placeholder='请输入关键字'
          dataSource={this.state.livebosObject.data
            .map(user => `[${user.OrgID}] ${user.Name} - ${user.Mobile || ''}`)}
          filterOption={(inputValue, option) =>
            (option.props.children as string).indexOf(inputValue) !== -1
          }
        />}
      </div>
    );
  }
}

export default ContactSearch;
