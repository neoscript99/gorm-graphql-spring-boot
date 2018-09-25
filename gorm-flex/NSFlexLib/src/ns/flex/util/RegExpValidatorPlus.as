package ns.flex.util
{
	import flash.display.DisplayObject;
	import mx.validators.RegExpValidator;

	public class RegExpValidatorPlus extends RegExpValidator
	{

		public var errorTip:String;

		public function RegExpValidatorPlus(source:DisplayObject, property:String='text',
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
			this.expression='.';
			this.required=false;
		}

		public function copyProperties(propertyHost:Object):void
		{
			if (propertyHost)
			{
				ObjectUtils.copyProperties(this, propertyHost);

				if (!propertyHost.noMatchError)
					noMatchError=
						(errorTip?errorTip:String("格式错误")).concat('(', expression, flags ? ' ' + flags : '',
						')');

				if (!propertyHost.requiredFieldError)
					requiredFieldError="不能为空";
			}
		}
	}
}

