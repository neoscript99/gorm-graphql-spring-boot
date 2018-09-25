package ns.flex.controls
{
	import mx.controls.Text;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.ClassFactory;
	import mx.utils.ObjectUtil;
	import ns.flex.common.Constants;
	import ns.flex.util.DateUtil;
	import ns.flex.util.ObjectUtils;
	import ns.flex.util.StringUtil;

	/**
	 * datagrid类对象增强，包括功能：1、支持dataField属性嵌套；2、支持数字设定小数点位数；
	 * 3、文字支持复制；4、标题默认自动换行。
	 * @author wangchu
	 */
	public class DataGridColumnPlus extends DataGridColumn
	{
		//部分不可选择，通过单独方法操作，如asAutoComplete、asComboBox
		[Inspectable(enumeration="Text,TextArea,CheckBox,DateString,LinkButton",
			defaultValue="Text", category="General")]
		public var asControl:String='Text';
		[Inspectable(category="General")]
		public var asNumber:Boolean=false;
		public var controlProps:Object; //for asComboBox
		[Inspectable(enumeration="sum,avg,max,min,none", defaultValue="",
			category="General")]
		public var groupMethod:String;
		[Inspectable(category="General")]
		public var isSeparateThousands:Boolean=true;
		public var searchControlIndex:int=-1;
		[Inspectable(enumeration="equal,%like%,like%,%like", defaultValue="none",
			category="General")]
		public var searchMethod:String='none';
		//use for set visiable,not work if is '*ignore*'
		public var type:String;
		//能否显示在datagrid的view pop中
		[Inspectable(category="General")]
		public var viewable:Boolean=true;
		// @see TextInputPlus,TextAreaPlus,DateFieldPlus
		private var _constraints:Object;
		private var _multEditable:Boolean=true;
		private var _multiplier:Number=1;
		private var _precision:int;

		public function DataGridColumnPlus(columnName:String=null)
		{
			super(columnName);
			headerWordWrap=true;
			itemRenderer=new ClassFactory(SelectableLabel);
		}

		public static function getNumberLabel(item:Object,
			column:DataGridColumnPlus):String
		{
			return StringUtil.formatNumber(column.getValue(item), column._precision,
				column.isSeparateThousands, column._multiplier ? column._multiplier : 1);
		}

		[Inspectable(category="General")]
		public function set asAutoComplete(props:Object):void
		{
			asControl='AutoComplete';
			controlProps=props;
		}

		/**
		 *
		 * @param info like {labelField:'',dataField:'',valueField:'id',dataProvider:[]}
		 */
		[Inspectable(category="General")]
		public function set asComboBox(props:Object):void
		{
			asControl='ComboBox';
			/*{labelField:'显示字段',
			   dataField:'返回字段，设置后返回selectedItem[dataField]，如果为空返回selectedItem',
			   valueField:'数据校验字段，设置后selectedItem[valueField]不为空时，ComboBoxPlus.validated返回true',
			   dataProvider:[]}*/
			controlProps=props;
		}

		[Inspectable(category="General")]
		public function set asDate(b:Boolean):void
		{
			if (b)
			{
				labelFunction=DateUtil.getDateLabel;
				asControl='DateField';
			}
		}

		[Inspectable(category="General")]
		public function set asLabelInput(props:Object):void
		{
			asAutoComplete=props;
			//标签，使用固定field
			dataField=Constants.LABEL_FIELD;
            visible=false;
		}

		[Inspectable(category="General")]
		public function set asTime(b:Boolean):void
		{
			if (b)
			{
				labelFunction=DateUtil.getTimeLabel;
				asControl='DateField';
			}
		}

		[Inspectable(category="General")]
		public function set asUploader(props:Object):void
		{
			asControl='Uploader';
			dataField=Constants.ATTACH_INFO_FIELD;
			visible=false;
			/*{ownerIdField:'附件所属对象ID',
			   ownerIdPrefix:'ID前缀，防止重复',
			   destination:'attachmentService',
			   maxFileNumber:5,
			   maxFileSize:1024 * 1024 * 10,
			   fileTypeFilter:[new FileFilter("Images", "*.jpg;*.gif;*.png"),new FileFilter("Documents", "*.pdf;*.doc;*.txt")]}*/
			controlProps=props;
		}

		[Inspectable(category="General")]
		/**
		 * 中文、数字排序，嵌套字段已根据中文排序，不用再在这里设置
		 * @param value
		 */
		public function set complexSort(value:Boolean):void
		{
			if (value)
				this.sortCompareFunction=complexColumnSortCompare;
		}

		public function get constraints():Object
		{
			return _constraints;
		}

		public function set constraints(value:Object):void
		{
			_constraints=
				(value is Array) ? ObjectUtils.mergeObjectArray(value as Array) : value;
		}

		public function getValue(item:Object):Object
		{
			return (!hasComplexFieldName) ? item[dataField] : deriveComplexColumnData(item);
		}

		override public function set labelFunction(value:Function):void
		{
			if (value != DataGridColumnPlus.getNumberLabel && asNumber)
				complexSort=true;
			super.labelFunction=value;
		}

		public function get multEditable():Boolean
		{
			return editable && _multEditable;
		}

		[Inspectable(category="General")]
		public function set multEditable(b:Boolean):void
		{
			_multEditable=b;
		}

		//乘数，显示万元、千元时有用
		public function get multiplier():Number
		{
			return _multiplier;
		}

		public function set multiplier(v:Number):void
		{
			if (isNaN(v) || v == 0)
				throw new Error('multiplier must be a number, and not 0');
			_multiplier=v;
			wordWrap=wordWrap; //refresh by owner.invalidateList();
		}

		public function get precision():int
		{
			return _precision;
		}

		public function set precision(p:int):void
		{
			_precision=p;
			asNumber=true;
			if (!groupMethod)
				groupMethod='sum';
			if (labelFunction == null)
				labelFunction=DataGridColumnPlus.getNumberLabel;
			else
				complexSort=true;
			this.setStyle('textAlign', 'right');
		}

		/**
		 * 默认使用SelectableLabel的truncateToFit
		 * 如果设wordWrap为ture，改用Text，可以多行，对话框控件改用TextArea
		 * 如果想保留truncateToFit，但对话框控件是TextArea，可设置asControl=TextArea
		 * @param value
		 */
		override public function set wordWrap(value:*):void
		{
			super.wordWrap=value;
			if (value == true)
				itemRenderer=new ClassFactory(Text);
		}

		/**
		 * 对嵌套字段、中文、数字进行排序
		 * @param obj1
		 * @param obj2
		 * @return
		 */
		override protected function complexColumnSortCompare(obj1:Object, obj2:Object):int
		{
			if (!obj1 && !obj2)
				return 0;

			if (!obj1)
				return 1;

			if (!obj2)
				return -1;

			if (!asNumber)
				return StringUtil.chineseCompare(itemToLabel(obj1), itemToLabel(obj2));
			else
				return ObjectUtil.numericCompare(Number(itemToLabel(obj1).replace(/,/g, '')),
					Number(itemToLabel(obj2).replace(/,/g, '')));
		}

		override protected function deriveComplexColumnData(data:Object):Object
		{
			return ObjectUtils.getValue(data, dataField);
		}
	}
}

