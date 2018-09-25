package ns.flex.support
{
	import flash.display.InteractiveObject;
	import flash.events.ContextMenuEvent;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import ns.flex.util.ArrayUtil;

	public class MenuSupport
	{
		//用于需要判断是否选择记录进行处理的控件,如datagrid等
		private var alwaysEnabledMap:Object={};
		private var contextMenu:ContextMenu;

		public function MenuSupport(interactiveObject:InteractiveObject,
			onMenuSelect:Function=null)
		{
			contextMenu=new ContextMenu();
			contextMenu.hideBuiltInItems();
			contextMenu.customItems=[];
			interactiveObject.contextMenu=contextMenu;

			if (onMenuSelect != null)
				interactiveObject.contextMenu.addEventListener(ContextMenuEvent.MENU_SELECT,
					onMenuSelect);
		}

		public function addMenuItem(item:ContextMenuItem, position:int=-1):void
		{
			if (position > -1)
				ArrayUtil.insertItem(item, position, contextMenu.customItems);
			else
				contextMenu.customItems.push(item);
		}

		public function clearCustomMenu():void
		{
			contextMenu.customItems=[];
		}

		/**
		 * 新菜单
		 * @param caption
		 * @param listener function (evt:ContextMenuEvent):void
		 * @param separatorBefore
		 * @param alwaysEnabled
		 * @param position 从0开始
		 * @return
		 */
		public function createMenuItem(caption:String, listener:Function,
			separatorBefore:Boolean=false, alwaysEnabled:Boolean=false,
			position:int=-1):ContextMenuItem
		{
			var menuItem:ContextMenuItem=
				new ContextMenuItem(caption, separatorBefore, alwaysEnabled);
			menuItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, listener);
			alwaysEnabledMap[getMenuCode(menuItem)]=alwaysEnabled;
			addMenuItem(menuItem, position);
			return menuItem;
		}

		public function isAlwaysEnabled(item:ContextMenuItem):Boolean
		{
			return alwaysEnabledMap[getMenuCode(item)];
		}

		public function removeMenuItem(property:String, value:*):void
		{
			ArrayUtil.removeItem(contextMenu.customItems,
				ArrayUtil.findByProperty(contextMenu.customItems, property, value));
		}

		private function getMenuCode(item:ContextMenuItem):String
		{
			return item.caption;
		}
	}
}

