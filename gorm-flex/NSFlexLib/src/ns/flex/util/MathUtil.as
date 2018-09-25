package ns.flex.util
{

	public class MathUtil
	{

		public static function avg(values:Array):Number
		{
			return values.length > 0 ? sum(values) / values.length : NaN;
		}

		public static function max(values:Array):Number
		{
			var max:Number;
			for each (var v:* in values)
			{
				var vnum:Number=Number(v);
				if (!isNaN(vnum) && (isNaN(max) || vnum > max))
					max=vnum;
			}
			return max;
		}

		public static function min(values:Array):Number
		{
			var min:Number;
			for each (var v:* in values)
			{
				var vnum:Number=Number(v);
				if (!isNaN(vnum) && (isNaN(min) || vnum < min))
					min=vnum;
			}
			return min;
		}

		public static function round(value:Number, precision:int):Number
		{
			var precisionPower:Number=Math.pow(10, precision);
			return (Math.round(value * precisionPower) / precisionPower)
		}

		public static function sum(values:Array):Number
		{
			var sum:Number=0;
			for each (var v:* in values)
			{
				var vnum:Number=Number(v);
				if (!isNaN(vnum))
					sum+=vnum;
			}
			return sum;
		}

		/**
		 * Number.toFixed 有bug ，(0.0000667676).toFixed(2) 显示 0.01
		 * 解决方法：http://stackoverflow.com/questions/632802/how-to-deal-with-number-precision-in-actionscript
		 * @param value
		 * @param precision
		 * @return
		 */
		public static function toFixed(value:Number, precision:int):String
		{
			return round(value, precision).toFixed(precision);
		}
	}
}

