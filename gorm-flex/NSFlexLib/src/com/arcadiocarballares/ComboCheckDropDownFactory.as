package com.arcadiocarballares
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import mx.controls.ComboBox;
	import mx.controls.List;
	import mx.controls.listClasses.*;
	
	public class ComboCheckDropDownFactory extends List
	{
		private var index:int=0;
		
		public function ComboCheckDropDownFactory():void
		{
			addEventListener("comboChecked", onComboChecked);
		}
		
		override protected function mouseOverHandler(event:MouseEvent):void
		{
			event.preventDefault();
			event.stopImmediatePropagation();
		}
		
		override protected function mouseEventToItemRenderer(event:MouseEvent):IListItemRenderer
		{
			var row:IListItemRenderer=super.mouseEventToItemRenderer(event);
			
			if (row != null)
			{
				index=itemRendererToIndex(row);
			}
			return null;
		}
		
		override protected function scrollHandler(event:Event):void
		{
			super.scrollHandler(event);
			refreshComboCheck();
		}
		
		override protected function scrollVertically(pos:int, deltaPos:int,
			scrollUp:Boolean):void
		{
			super.scrollVertically(pos, deltaPos, scrollUp);
			refreshComboCheck();
		}
		
		private function refreshComboCheck():void
		{
			ComboBox(owner).invalidateProperties();
		}
		
		private function onComboChecked(event:ComboCheckEvent):void
		{
			var myComboCheckEvent:ComboCheckEvent=
				new ComboCheckEvent(ComboCheckEvent.COMBO_CHECKED);
			myComboCheckEvent.obj=ComboCheckEvent(event).obj;
			owner.dispatchEvent(myComboCheckEvent);
		}
	}
}