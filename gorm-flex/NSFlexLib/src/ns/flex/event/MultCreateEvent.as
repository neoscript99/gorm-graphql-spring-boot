package ns.flex.event
{
	import flash.events.Event;

	public class MultCreateEvent extends Event
	{
		public var newList:Array;

		public function MultCreateEvent(newList:Array, bubbles:Boolean=false,
			cancelable:Boolean=false)
		{
			this.newList=newList;
			super('multCreate', bubbles, cancelable);
		}
	}
}

