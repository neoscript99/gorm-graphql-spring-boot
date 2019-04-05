import React, { Component, Fragment } from 'react'
import { Layout, Menu, Breadcrumb, Icon } from 'antd'
import { observer } from 'mobx-react'
import MenuStore, { MenuNode } from '../stores/MenuStore';

const {
  Header, Content, Footer, Sider
} = Layout

const SubMenu = Menu.SubMenu

@observer
class Home extends Component<{ menuStore: MenuStore }, { collapsed: boolean }> {
  state = {
    collapsed: false,
  }

  onCollapse = (collapsed: boolean) => {
    console.log(collapsed)
    this.setState({ collapsed })
  }

  render() {
    const { menuStore } = this.props;
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Header>
          <div className="logo" />
        </Header>
        <Layout>
          <Sider
            collapsible
            collapsed={this.state.collapsed}
            onCollapse={this.onCollapse}
            theme="light"
          >
            <Menu theme="light" defaultSelectedKeys={['1']} mode="inline">
              {menuStore.menuTree.subMenus.map((menuNode) => {
                  return menuNode.menu.app ? (
                      <Menu.Item key={menuNode.menu.app}><Icon type="file" /><span>{menuNode.menu.label}</span></Menu.Item>)
                    : (<SubMenu
                      key={menuNode.menu.id}
                      title={<span><Icon type="folder" /><span>{menuNode.menu.label}</span></span>}>
                      {menuNode.subMenus.map((subNode) => (<Menu.Item key={subNode.menu.app}><Icon
                        type="file" /><span>{subNode.menu.label}</span></Menu.Item>))}
                    </SubMenu>)
                }
              )}
            </Menu>
          </Sider>
          <Layout>
            <Content style={{ margin: '0 16px' }}>
              <Breadcrumb style={{ margin: '16px 0' }}>
                <Breadcrumb.Item>User</Breadcrumb.Item>
                <Breadcrumb.Item>Bill</Breadcrumb.Item>
              </Breadcrumb>
              <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
                Bill is a cat.
              </div>
            </Content>
            <Footer style={{ textAlign: 'center' }}>
              Ant Design ©2018 Created by Ant UED
            </Footer>
          </Layout>
        </Layout>
      </Layout>
    )
  }
}

export default Home
