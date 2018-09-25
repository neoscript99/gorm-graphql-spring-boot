/*
 * ComboCheck
 * v1.2.5
 * Arcadio Carballares Martín, 2009
 * http://www.carballares.es/arcadio
 */
package com.arcadiocarballares
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.core.ClassFactory;
	import mx.events.DropdownEvent;
	import mx.events.FlexEvent;
	import ns.flex.util.ArrayCollectionPlus;

	[Event(name="changedAndClose", type="flash.events.Event")]
	[Event(name="selectAll", type="flash.events.Event")]
	[Event(name="deSelectAll", type="flash.events.Event")]
	public class ComboCheck extends ComboBox
	{
		public static const SELECTED_FIELD:String='ComboCheckSelectedField';
		public static const VALUE_FIELD:String='ComboCheckValueField';

		public var defaultLabels:Object;
		public var itemAllValue:int=-1;
		private var _selectedItems:ArrayCollection;
		private var selectChanged:Boolean=false;

		public function ComboCheck()
		{
			super();
			rowCount=15;
			addEventListener("comboChecked", onComboChecked);
			addEventListener(FlexEvent.CREATION_COMPLETE, refresh);
			addEventListener(Event.ADDED_TO_STAGE, refresh);
			addEventListener(DropdownEvent.CLOSE, onClose);
			addEventListener(DropdownEvent.OPEN, onOpen);
		}

		override public function set dataProvider(value:Object):void
		{
			var itemAll:Object={};
			itemAll[VALUE_FIELD]=itemAllValue;
			decorateItemAll(itemAll);
			var acp:ArrayCollectionPlus=new ArrayCollectionPlus(value);
			acp.addItemAt(itemAll, 0);
			super.dataProvider=acp;

			// Set selecAll position
			if (defaultLabels)
				selectLabels(defaultLabels);
			else
				for (var i:int; i < dataProvider.length; i++)
					dataProvider[i][SELECTED_FIELD]=false;
		}

		public function getSelectFieldArray(field:String):Array
		{
			var arr:Array=[];

			for each (var item:Object in dataProvider)
				if (item[field] && item[SELECTED_FIELD])
					arr.push(item[field]);
			return arr;
		}

		public function isAllSelect():Boolean
		{
			return dataProvider[0][SELECTED_FIELD];
		}

		public function selectAll(sel:Boolean):void
		{
			for each (var item:Object in dataProvider)
			{
				item[SELECTED_FIELD]=sel;
			}
			changeCommit();

			if (sel)
				dispatchEvent(new Event("selectAll"));
			else
				dispatchEvent(new Event("deSelectAll"));
		}

		/**
		 * 下拉框默认选择的项目
		 * @param labels 选择标签中包含labels的项目，如果labels包含''，选择全部
		 */
		public function selectLabels(labels:Object):void
		{
			defaultLabels=labels;

			if (labels == null)
				return;
			var labelArray:Array;

			if (labels is String)
				labelArray=(labels as String).split(',');
			else
				labelArray=(labels as Array);

			for each (var item:Object in dataProvider)
			{
				var itemLabel:String=itemToLabel(item);
				var selected:Boolean=false;

				for each (var label:String in labelArray)
					if (itemLabel.indexOf(label) > -1)
					{
						selected=true;
						break;
					}
				item[SELECTED_FIELD]=selected;
			}
			changeCommit();
		}

		[Bindable("change")]
		[Bindable("valueCommit")]
		[Bindable("collectionChange")]
		public function get selectedItems():ArrayCollection
		{
			return _selectedItems;
		}

		public function set selectedItems(value:ArrayCollection):void
		{
			_selectedItems=value;
		}

		override protected function commitProperties():void
		{
			super.commitProperties();
			checkSelectAll();
			itemRenderer=new ClassFactory(ComboCheckItemRenderer);
			dropdownFactory=new ClassFactory(ComboCheckDropDownFactory);
			selectedItems=new ArrayCollection();

			for each (var item:Object in dataProvider)
				if (item[SELECTED_FIELD] == true)
					selectedItems.addItem(item);
			setText();
		}

		private function changeCommit():void
		{
			dispatchEvent(new Event("valueCommit"));
			invalidateProperties();
		}

		private function checkSelectAll():void
		{
			var allSelected:Boolean=true;

			for (var i:int=1; i < dataProvider.length; i++)
				if (dataProvider[i][SELECTED_FIELD] == false)
				{
					allSelected=false;
					break;
				}
			dataProvider[0][SELECTED_FIELD]=allSelected;
		}

		private function decorateItemAll(item:Object, count:int=0):void
		{
			if (labelField)
				item[labelField]='全选';
			else if (count < 10)
				callLater(decorateItemAll, [item, count + 1]);
		}

		private function onClose(event:DropdownEvent):void
		{
			if (selectChanged)
				dispatchEvent(new Event('changedAndClose'));
			selectChanged=false;
		}

		private function onComboChecked(event:ComboCheckEvent):void
		{
			var obj:Object=event.obj;
			selectChanged=true;

			if (obj[VALUE_FIELD] == itemAllValue)
			{
				selectAll(obj[SELECTED_FIELD]);
				return;
			}
			changeCommit();
		}

		private function onDropdownRollOut(e:MouseEvent):void
		{
			close();
		}

		/**
		 * every dropdown open, there is a new instance.
		 */
		private function onOpen(event:Event):void
		{
			dropdown.addEventListener(MouseEvent.ROLL_OUT, onDropdownRollOut);
		}

		private function refresh(event:Event):void
		{
			invalidateProperties();
		}

		private function setText():void
		{
			if (selectedItems.length == dataProvider.length)
			{
				textInput.text='全选'
			}
			else if (selectedItems.length > 1)
			{
				textInput.text='多选'
			}
			else if (selectedItems.length == 1)
			{
				textInput.text=itemToLabel(selectedItems.getItemAt(0));
			}
			else if (selectedItems.length < 1)
			{
				textInput.text='请选择';
			}
		}
	}
}

