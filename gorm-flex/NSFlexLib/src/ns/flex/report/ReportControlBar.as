package ns.flex.report
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import mx.containers.ApplicationControlBar;
	import ns.flex.util.ContainerUtil;

	[Event(name="export")]
	[Event(name="change")]
	public class ReportControlBar extends ApplicationControlBar
	{
		public function get queryParam():Object
		{
			return null;
		}

		protected function change():void
		{
			dispatchEvent(new Event('change'))
		}
	}
}

