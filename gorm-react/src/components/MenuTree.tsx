import React from 'react'
import { Menu, Icon } from 'antd'
import { Link } from 'react-router-dom';
import { MenuNode } from '../stores/MenuStore';

const SubMenu = Menu.SubMenu

function getTree(menuNode: MenuNode) {
  if (menuNode.menu.app)
    return (
      <Menu.Item key={menuNode.menu.id}>
        <Icon type="file" />
        <Link to={`/${menuNode.menu.app}/`} style={{ display: 'inline' }}>{menuNode.menu.label}</Link>
      </Menu.Item>)
  else
    return (<SubMenu key={menuNode.menu.id} title={<span><Icon type="folder" />{menuNode.menu.label}</span>}>
      {menuNode.subMenus.map((subNode) => getTree(subNode))}
    </SubMenu>)
}

export default ({ rootMenu }: { rootMenu: MenuNode }) =>
  <Menu theme="light" defaultSelectedKeys={['1']} mode="inline">
    {rootMenu.subMenus.map((menuNode) => getTree(menuNode))}
  </Menu>
