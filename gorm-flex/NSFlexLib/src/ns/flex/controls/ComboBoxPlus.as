package ns.flex.controls
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import mx.collections.IList;
	import mx.controls.ComboBox;
	import mx.events.ListEvent;
	import ns.flex.util.ArrayCollectionPlus;
	import ns.flex.util.UIUtil;

	[Event(name="levelDown")]
	[Event(name="levelUp")]
	public class ComboBoxPlus extends ComboBox
	{
		[Inspectable(category="General")]
		public var repeatable:Boolean=true;
		public var valueField:String;
		private var _defaultLabel:String;
		private var borderColorOrigin:String;
		private var changeByWheel:Boolean=false;

		public function ComboBoxPlus()
		{
			super();
			tabEnabled=false;
			rowCount=15;
			addEventListener(MouseEvent.MOUSE_WHEEL, onMouseWheel);
			addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
		}

		public function get defaultLabel():String
		{
			return _defaultLabel;
		}

		public function set defaultLabel(value:String):void
		{
			this._defaultLabel=value;
			invalidateProperties();
		}

		/**
		 * 当ComboBox在popwindow中多次弹出时，无法记忆上次的选择结果
		 * 但如果ComboBox在module中，再通过popwindow弹出时，没有这个问题
		 */
		public function reserveSelect():void
		{
			_defaultLabel=selectedLabel;
		}

		public function selectItemByField(value:*, field:String=null):void
		{
			if (!field)
				field=labelField;
			var findItem:Object=
				new ArrayCollectionPlus(dataProvider).findByField(field, value);
			if (findItem && selectedItem != findItem)
			{
				selectedItem=findItem;
				dispatchEvent(new ListEvent('change'))
			}
		}

		override public function set selectedIndex(value:int):void
		{
			if (dataProvider && dataProvider.length > 0)
				if (value < 0)
				{
					if (repeatable)
					{
						super.selectedIndex=dataProvider.length - 1
						dispatchEvent(new Event('levelDown'));
					}
				}
				else if (value > dataProvider.length - 1)
				{
					if (repeatable)
					{
						super.selectedIndex=0
						dispatchEvent(new Event('levelUp'));
					}
				}
				else
					super.selectedIndex=value
		}

		[Bindable("valueCommit")]
		public function get validated():Boolean
		{
			var vld:Boolean=false;
			if (selectedItem)
				vld=valueField ? selectedItem[valueField] : true;
			UIUtil.setBorderByValidate(this, vld)
			return vld;
		}

		public function set withIndex(v:Boolean):void
		{
			if (v)
				labelFunction=getIndexLabel;
		}

		override protected function commitProperties():void
		{
			super.commitProperties();
			if (_defaultLabel)
				selectDefaultLabel();
		}

		private function getIndexLabel(item:Object):String
		{
			return (dataProvider is IList) ? String(IList(dataProvider).getItemIndex(item) + 1).concat('、',
				item[labelField]) : item[labelField];
		}

		private function onMouseOut(event:MouseEvent):void
		{
			if (changeByWheel)
			{
				changeByWheel=false;
				dispatchEvent(new ListEvent('change'));
			}
		}

		private function onMouseWheel(event:MouseEvent):void
		{
			changeByWheel=true;
			selectedIndex-=Math.abs(event.delta) / event.delta;
		}

		private function selectDefaultLabel():void
		{
			var tempIndex:int=-1;

			for (var i:int=dataProvider.length - 1; i >= 0; i--)
			{
				var label:String=itemToLabel(dataProvider[i]);

				if (label == _defaultLabel)
				{
					tempIndex=i;
					break;
				}
				//如果没有完全相等的，查找包含的，最终会选择最后一个包含的
				else if (label.indexOf(_defaultLabel) > -1)
				{
					tempIndex=i;
				}
			}

			if (tempIndex > -1)
				selectedIndex=tempIndex;
		}
	}
}

