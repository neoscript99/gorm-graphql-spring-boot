package ns.flex.util
{
	
	public interface Validatable
	{
		function set constraints(value:Object):void;
		function get validated():Boolean;
	}
}