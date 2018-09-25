package ns.flex.event
{
	import flash.events.Event;

	import ns.flex.controls.PopWindow;

	public class ShowItemEvent extends Event
	{
		public var editable:Boolean;
		public var pop:PopWindow;
		public var showItem:Object;

		public function ShowItemEvent(item:Object, able:Boolean, p:PopWindow)
		{
			showItem=item;
			editable=able;
			pop=p;
			super('showItem');
		}
	}
}

