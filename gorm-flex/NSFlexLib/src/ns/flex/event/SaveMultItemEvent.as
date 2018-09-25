package ns.flex.event
{
	import flash.events.Event;
	import mx.utils.ObjectProxy;

	public class SaveMultItemEvent extends Event
	{
		public var multItems:Array;

		public function SaveMultItemEvent(multItems:Array, bubbles:Boolean=false,
			cancelable:Boolean=false)
		{
			this.multItems=multItems;
			super('saveMultItem', bubbles, cancelable);
		}
	}
}
