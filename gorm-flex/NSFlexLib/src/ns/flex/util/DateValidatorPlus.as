package ns.flex.util
{
	import flash.display.DisplayObject;
	import mx.validators.DateValidator;

	/**
	 * 日期校验
	 * @author wangchu
	 */
	public class DateValidatorPlus extends DateValidator
	{
		public function DateValidatorPlus(source:DisplayObject, property:String='text',
			trigger:DisplayObject=null, triggerEvent:String='valueCommit')
		{
			super();
			this.source=source;
			this.property=property;

			if (trigger)
				this.trigger=trigger;
			else
				this.trigger=source;
			this.triggerEvent=triggerEvent;
			property="text";
			triggerEvent='valueCommit';
			validateAsString="true";
			inputFormat="YYYYMMDD";
			//allowedFormatChars='';
			formatError="日期格式错误";
			invalidCharError="请不要输入除数字和分隔符外的字符"
			wrongLengthError="日期长度不正确";
			wrongDayError="根据当前月份，请输入正确的日期.";
			wrongMonthError="请输入正确的月份 1 - 12.";
			wrongYearError="请输入正确的年度 0 - 9999.";
			requiredFieldError='不能为空';
			required=true;
		}
	}
}

