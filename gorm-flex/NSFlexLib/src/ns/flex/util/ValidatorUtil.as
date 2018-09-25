package ns.flex.util
{
	import mx.events.ValidationResultEvent;
	import mx.validators.Validator;
	
	public class ValidatorUtil
	{
		public static function validate(validator:Validator):Boolean
		{
			return validator ? (validator.validate().type == ValidationResultEvent.VALID) : true;
		}
		
		public static function validateAll(validatorArray:Array):Boolean
		{
			var result:Boolean=true;
			
			for each (var validator:Validator in validatorArray)
				if (!validate(validator))
					result=false;
			return result;
		}
	
	}
}