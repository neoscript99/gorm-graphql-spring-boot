package ns.flex.event
{
	import flash.events.Event;
	import mx.utils.ObjectProxy;
	
	public class SaveItemEvent extends Event
	{
		public var saveItem:ObjectProxy;
		
		public function SaveItemEvent(item:ObjectProxy, bubbles:Boolean=false,
			cancelable:Boolean=false)
		{
			saveItem=item;
			super('saveItem', bubbles, cancelable);
		}
	}
}