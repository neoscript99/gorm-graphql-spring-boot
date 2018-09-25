package ns.flex.event
{
	import flash.events.Event;
	
	/**
	 * 换页事件
	 * @author wangchu
	 */
	public class PageChangeEvent extends Event
	{
		public static const TYPE:String="changePage";
		public var first:int;
		
		public function PageChangeEvent(first:int)
		{
			//bubbles为true才会冒泡到上级容器
			super(TYPE, true);
			this.first=first;
		}
	}
}