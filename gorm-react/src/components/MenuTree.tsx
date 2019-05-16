import React from 'react'
import { Menu, Icon } from 'antd'
import { Link } from 'react-router-dom';
import { MenuNode } from '../stores/MenuStore';

const SubMenu = Menu.SubMenu

function getTree(menuNode: MenuNode, pathPrefix: string) {
  if (menuNode.menu.app)
    return (
      <Menu.Item key={menuNode.menu.id}>
        <Icon type="file" />
        <Link to={`${pathPrefix}${menuNode.menu.app}/`} style={{ display: 'inline' }}>{menuNode.menu.label}</Link>
      </Menu.Item>)
  else
    return (<SubMenu key={menuNode.menu.id} title={<span><Icon type="folder" />{menuNode.menu.label}</span>}>
      {menuNode.subMenus.map((subNode) => getTree(subNode, pathPrefix))}
    </SubMenu>)
}

interface P {
  rootMenu: MenuNode
  pathPrefix: string
}

export default ({ rootMenu, pathPrefix }: P) =>
  <Menu theme="light" defaultSelectedKeys={['1']} mode="inline">
    {rootMenu.subMenus.map((menuNode) => getTree(menuNode, pathPrefix))}
  </Menu>
