package ns.flex.util
{
	import flash.display.DisplayObjectContainer;
	import flash.display.IBitmapDrawable;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.geom.Point;
	import flash.net.FileReference;
	import flash.utils.Dictionary;
	import mx.binding.utils.BindingUtils;
	import mx.controls.ToolTip;
	import mx.core.UIComponent;
	import mx.core.UITextField;
	import mx.graphics.ImageSnapshot;
	import mx.managers.ToolTipManager;

	public class UIUtil
	{
		private static const tipMap:Dictionary=new Dictionary;
		private static const borderMap:Dictionary=new Dictionary;

		public static function destroyTip(e:Event):void
		{
			var tip:ToolTip=tipMap[e.currentTarget];
			if (tip)
				ToolTipManager.destroyToolTip(tip);
		}

		public static function setBorderByValidate(uic:UIComponent,validated:Boolean):void
		{
			if (borderMap[uic])
				borderMap[uic]=uic.getStyle('borderColor')
			if (validated)
				uic.setStyle('borderColor', borderMap[uic]);
			else
				uic.setStyle('borderColor', 'red');
		}
		/*
		 * 隐藏amchart免费版链接
		 */
		public static function removeAmLink(container:DisplayObjectContainer):void
		{
			trace('removeAmLink')
			var link:UITextField=
				ContainerUtil.findContainerChild(container, UITextField, 'text',
				"chart by amCharts.com") as UITextField
			if (link)
			{
				link.htmlText=''
				link.text=''
				link.visible=false
				link.parent.visible=false
			}
		}

		public static function setEvents(ed:EventDispatcher, events:Object):void
		{
			for (var event:* in events)
				ed.addEventListener(event, events[event])
		}

		public static function setStyles(uic:UIComponent, styles:Object):void
		{
			for (var style:* in styles)
				uic.setStyle(style, styles[style])
		}

		public static function showSizeTip(e:Event):void
		{
			//取得绝对坐标
			var p:Point=e.currentTarget.localToGlobal(new Point);
			var tip:ToolTip=
				ToolTipManager.createToolTip('', p.x + e.currentTarget.width + 5,
				p.y) as ToolTip;
			tipMap[e.currentTarget]=tip;

			var textInput:Object=e.currentTarget;
			BindingUtils.bindSetter(function(value:Object):void
			{
				tip.text=String(value).concat('/', textInput.maxChars);
			}, e.currentTarget, 'remainSize');
		}

		public static function snapshot(target:IBitmapDrawable, fileName:String=null):void
		{
			new FileReference().save(ImageSnapshot.captureImage(target).data,
				(fileName ? fileName : ('Image' + new Date().getTime())) + '.png');
		}
	}
}

