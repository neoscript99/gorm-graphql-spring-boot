package ns.flex.controls
{
import flash.events.Event;

import mx.containers.ControlBar;
	import mx.controls.NumericStepper;
	import ns.flex.event.PageChangeEvent;
	import ns.flex.util.ContainerUtil;

	[Event(name="changePage", type="ns.flex.event.PageChangeEvent")]
	public class PagerBase extends ControlBar
	{
		[Bindable]
		protected var _offsetValue:int=15;

		[Bindable]
		protected var curPage:int=0;
		protected var map:Object={};

		public function get first():int
		{
			return (curPage - 1) * offsetStepper.value
		}

		/*
		 *检查当前查询的firstResult是否小于0，是的话标识重置当前页
		 */
		public function forFirst(first:int):int
		{
			var realFirst:int=first > 0 ? first : 0;
			curPage=realFirst / offsetStepper.value + 1;
			return realFirst;
		}

		/*
		 *向前或向后多少页
		 *num可以为负数
		 */
		public function go(num:int):void
		{
			gotoPage(curPage + num);
		}

		/*
		 *跳转到某页，触发换页事件
		 */
		public function gotoPage(pageIndex:int):void
		{
			curPage=pageIndex;
			dispatchEvent(new PageChangeEvent((curPage - 1) * offsetValue));
		}

		/**
		 * 不能取_offsetValue,因为stepSize的原因，实际offsetStepper.value可能不等于_offsetValue
		 * @return
		 */
		public function get offsetValue():int
		{
			return offsetStepper.value;
		}

		public function set offsetValue(value:int):void
		{
			_offsetValue=value;
		}

		/*
		 *刷新当前页
		 */
		public function refresh(e:Event=null):void
		{
			go(0)
		}

		protected function changeStepper(value:int):void
		{
			go(0);
		}

		protected function get offsetStepper():NumericStepper
		{
			if (!map.offsetStepper)
				map.offsetStepper=ContainerUtil.findContainerChild(this, NumericStepper);
			return map.offsetStepper;
		}
	}
}

