package ns.flex.controls
{
	import mx.controls.DateField;
	import ns.flex.util.DateUtil;
	import ns.flex.util.DateValidatorPlus;
	import ns.flex.util.ObjectUtils;
	import ns.flex.util.Validatable;
	import ns.flex.util.ValidatorUtil;

	public class DateFieldPlus extends DateField implements Validatable
	{
		private var _defaultDate:String='today';
		private var today:Date=new Date();
		private var validator:DateValidatorPlus;

		public function DateFieldPlus()
		{
			super();
			yearNavigationEnabled=true;
			firstDayOfWeek=1;
			width=100;
			yearSymbol='年';
			monthNames=
				['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
			formatString='YYYYMMDD';
			dayNames=['日', '一', '二', '三', '四', '五', '六'];
			resetDefault();
		}

		public function set constraints(value:Object):void
		{
			if (value)
			{
				if (!validator)
					validator=new DateValidatorPlus(this);
				ObjectUtils.copyProperties(this, value);
				ObjectUtils.copyProperties(validator, value);
			}
		}

		[Inspectable(enumeration="today,yesterday,last_month_final,last_year_final,none",
			defaultValue="today", category="General")]
		public function set defaultDate(dd:String):void
		{
			_defaultDate=dd;
			resetDefault();
		}

		override public function set editable(value:Boolean):void
		{
			super.editable=value;

			if (!validator)
				validator=new DateValidatorPlus(this);

		}

		override public function set formatString(value:String):void
		{
			super.formatString=value;
			if (!validator)
			{
				validator=new DateValidatorPlus(this);
				validator.required=false;
			}
			validator.inputFormat=value;
		}

		public function getTomorrow():Date
		{
			return DateUtil.shiftDays(selectedDate, 1);
		}

		public function getYesterday():Date
		{
			return DateUtil.shiftDays(selectedDate, -1);
		}

		public function resetDefault():void
		{
			switch (_defaultDate)
			{
				case 'today':
					selectedDate=today;
					break;
				case 'yesterday':
					selectedDate=DateUtil.shiftDays(today, -1);
					break;
				case 'last_month_final':
					selectedDate=DateUtil.getLastMonthFinal(today);
					break;
				case 'last_year_final':
					selectedDate=DateUtil.getLastYearFinal(today);
					break;
				default:
					selectedDate=null;
			}
		}

		[Bindable("valueCommit")]
		public function get validated():Boolean
		{
			return ValidatorUtil.validate(validator);
		}
	}
}

