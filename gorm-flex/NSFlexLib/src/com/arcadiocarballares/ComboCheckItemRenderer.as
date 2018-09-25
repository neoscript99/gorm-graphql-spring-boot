package com.arcadiocarballares
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import mx.controls.CheckBox;
	import mx.events.FlexEvent;
	
	public class ComboCheckItemRenderer extends CheckBox
	{
		public function ComboCheckItemRenderer()
		{
			super();
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
			addEventListener(MouseEvent.CLICK, onClick);
		}
		
		private function onCreationComplete(event:Event=null):void
		{
			if (data != null && data[ComboCheck.SELECTED_FIELD] == true)
			{
				selected=true;
			}
		}
		
		private function onClick(event:MouseEvent):void
		{
			data[ComboCheck.SELECTED_FIELD]=selected;
			var myComboCheckEvent:ComboCheckEvent=
				new ComboCheckEvent(ComboCheckEvent.COMBO_CHECKED);
			myComboCheckEvent.obj=data;
			owner.dispatchEvent(myComboCheckEvent);
		}
	}
}