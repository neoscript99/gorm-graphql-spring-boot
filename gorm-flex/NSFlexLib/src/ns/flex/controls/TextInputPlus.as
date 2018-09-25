package ns.flex.controls
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.system.IME;
	import mx.controls.TextInput;
	import mx.events.FlexEvent;
	import ns.flex.util.ObjectUtils;
	import ns.flex.util.RegExpValidatorPlus;
	import ns.flex.util.StringUtil;
	import ns.flex.util.UIUtil;
	import ns.flex.util.Validatable;
	import ns.flex.util.ValidatorUtil;

	public class TextInputPlus extends TextInput implements Validatable
	{

		[Inspectable(category="General")]
		public var autoTrim:Boolean=true;
		public var ignorePattern:RegExp;

		[Inspectable(enumeration="true,false", defaultValue="true", category="General")]
		public var imeDisabled:Boolean=false;
		[Inspectable(category="General")]
		public var noSpace:Boolean=false;
		[Inspectable(category="General")]
		public var showSizeTip:Boolean=true;
		private const THRESHOLD_SIZE:int=32;

		private var validator:RegExpValidatorPlus;

		public function TextInputPlus()
		{
			super();
			addEventListener(FlexEvent.VALUE_COMMIT, onValueCommit);
			addEventListener(MouseEvent.ROLL_OVER, UIUtil.showSizeTip);
			addEventListener(MouseEvent.ROLL_OUT, UIUtil.destroyTip);
			maxChars=32;
		}

		public function set constraints(value:Object):void
		{
			if (value)
			{
				if (!validator)
					validator=new RegExpValidatorPlus(this);
				ObjectUtils.copyProperties(this, value);

				if (maxChars > THRESHOLD_SIZE)
					width=Math.min(maxChars / THRESHOLD_SIZE, 3) * 160;
				validator.copyProperties(value);
			}
		}

		[Bindable("textChanged")]
		[Bindable("maxCharsChanged")]
		public function get remainSize():int
		{
			return maxChars - text.length;
		}

		[Bindable("valueCommit")]
		public function get validated():Boolean
		{
			return ValidatorUtil.validate(validator);
		}

		override protected function focusInHandler(event:FocusEvent):void
		{
			super.focusInHandler(event);
			IME.enabled=!imeDisabled;
		}

		override protected function focusOutHandler(event:FocusEvent):void
		{
			super.focusOutHandler(event);
			IME.enabled=true;
		}

		protected function onValueCommit(event:Event):void
		{
			if (noSpace)
				text=StringUtil.removeSpace(text);
			else if (autoTrim)
				text=StringUtil.trim(text);

			if (ignorePattern)
				text=text.replace(ignorePattern, '');
		}
	}
}

