package ns.flex.controls
{
	import flash.events.Event;
	
	import mx.containers.ApplicationControlBar;
	import mx.controls.Button;
	import mx.controls.CheckBox;
	import mx.controls.DateField;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import ns.flex.common.Messages;
	import ns.flex.util.ArrayCollectionPlus;
	import ns.flex.util.ObjectUtils;
	import ns.flex.util.StringUtil;

	[Event(name="query")]
	public class AppBarPlus extends ApplicationControlBar
	{

		public var dataProvider:DataGridPlus;
		public var orders:Array=[];
		public var queryParam:Object;
		//用来生成DataColumnFormItem和存储输入数据，不在界面展示
		private var dgp:DataGridPlus=new DataGridPlus;

		public function AppBarPlus()
		{
			super();
			addEventListener(FlexEvent.CREATION_COMPLETE, cc);
		}

		public function set searchItem(item:Object):void
		{
			dgp.showItem=item;
		}

		private function cc(e:Event):void
		{
			if (!dataProvider)
			{
				return;
			}
			for each (var col:DataGridColumn in dataProvider.columns)
				if (col is DataGridColumnPlus && col.dataField &&
					col['searchMethod'] != 'none')
					makeFormItem(DataGridColumnPlus(col));

			var queryButton:Button=new Button();
			queryButton.label=Messages.QUERY;
			queryButton.addEventListener('click', function(e:Event):void
			{
				queryParam=emptyParam;
				queryParam.order=orders;
				dispatchEvent(new Event('query'));
			});
			var resetButton:Button=new Button();
			resetButton.label=Messages.RESET;
			resetButton.addEventListener('click', function(e:Event):void
			{
				searchItem=null;
			});

			addChild(queryButton);
			addChild(resetButton);
		}

		private function get emptyParam():Object
		{
			return {eq: [], like: []};
		}

		private function makeFormItem(colp:DataGridColumnPlus):void
		{
			var dcfi:DataColumnFormItem=new DataColumnFormItem(dgp, colp, true, false);
			if (colp.asControl == 'ComboBox')
			{
				var cbp:ComboBoxPlus=ComboBoxPlus(dcfi.component);
				if (cbp.dataProvider)
				{
					var ac:ArrayCollectionPlus=
						ArrayCollectionPlus.withAll(cbp.dataProvider, cbp.labelField)
					if (ac[1][cbp.labelField] == Messages.ASK_TO_CHOOSE)
						ac.removeItemAt(1);
					cbp.dataProvider=ac;
				}
			}
			if (colp.searchMethod.indexOf('like') > -1)
				dcfi.component.toolTip=colp.searchMethod.replace('like', 'xx');
			if (colp.searchControlIndex > -1)
				addChildAt(dcfi, colp.searchControlIndex)
			else
				addChild(dcfi);
			addEventListener('query', function(e:Event):void
			{
				makeParam(colp, dcfi.component);
			}, false, 100);
		}

		private function makeParam(colp:DataGridColumnPlus, uic:UIComponent):void
		{
			var value:*;
			if (colp.asControl == 'CheckBox')
			{
				value=(uic as CheckBox).selected;
			}
			else if (colp.asControl == 'ComboBox')
			{
				var cbp:ComboBoxPlus=ComboBoxPlus(uic);
				if (cbp.valueField)
					field=cbp.valueField;
				value=cbp.selectedItem[field];
			}
			else if (colp.asControl == 'DateField')
			{
				value=(uic as DateField).selectedDate;
			}
			else
			{
				value=uic['text'];
			}

			trace(colp.headerText, value, StringUtil.trim(value));
			if (StringUtil.trim(value).length > 0)
			{
				var param:Object=queryParam;
				var lastDot:int=colp.dataField.lastIndexOf('.');
				var path:String='';
				var field:String=colp.dataField;
				if (lastDot > -1)
				{
					path=colp.dataField.slice(0, lastDot);
					field=colp.dataField.slice(lastDot + 1);
					param=emptyParam;
					ObjectUtils.setValue(queryParam, path, param);
				}
				if (colp.searchMethod == 'equal')
					(param.eq as Array).push([field, value]);
				else if (colp.searchMethod.indexOf('like') > -1)
					(param.like as Array).push([field,
						colp.searchMethod.replace('like', value)]);
			}
		}
	}
}
