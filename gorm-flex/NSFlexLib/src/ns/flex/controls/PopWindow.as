package ns.flex.controls
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	import mx.managers.SystemManager;

	public class PopWindow extends TitleWindowPlus
	{

		//这个功能应该只在针对静态内容时开启，
		//如果内容为动态，width或height被赋值后，窗口大小就不会跟着内容变化
		[Inspectable(category="General")]
		public var tryToRemoveScrollBar:Boolean=true;
		private var originHeight:Number;
		private var originWidth:Number;
		private var originX:Number;
		private var originY:Number;
		private var popProgress:ProgressBox;

		public function PopWindow()
		{
			super();
			showCloseButton=true
			addEventListener(KeyboardEvent.KEY_DOWN, onKeyDown);
			addEventListener(FlexEvent.CREATION_COMPLETE, cc);
			addEventListener(CloseEvent.CLOSE, onClose);
			addEventListener(Event.ADDED, onFocus);
			addEventListener(MouseEvent.CLICK, onFocus);
			addEventListener('titleDoubleClick', switchSize);

			maxWidth=SystemManager.getSWFRoot(this).stage.stageWidth * .9;
			maxHeight=SystemManager.getSWFRoot(this).stage.stageHeight * .9;
		}

		public function center():void
		{
			if (this.isPopUp)
				PopUpManager.centerPopUp(this);
		}

		public function close():void
		{
			this.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
			trace('CloseEvent');
		}

		public function closeProgress():void
		{
			if (popProgress && popProgress.isPopUp)
				popProgress.close();
		}

		public function show(parent:DisplayObject, modal:Boolean=true):void
		{
			if (!this.isPopUp)
				PopUpManager.addPopUp(this, parent, modal);
		}

		public function showProgress():void
		{
			if (!popProgress)
				popProgress=new ProgressBox();
			popProgress.show(this);
		}

		private function cc(e:Event):void
		{
			//height最大时，width未最大，出现横竖滚动条，但横向其实还有扩展空间，加大width，去除横向滚动
			//width最大时，同理.
			//这个功能应该只在针对静态内容时开启，
			//如果内容为动态，width或height被赋值后，窗口大小就不会跟着内容变化
			if (tryToRemoveScrollBar)
			{
				if (height == maxHeight && width < maxWidth && this.verticalScrollBar)
					width+=this.verticalScrollBar.width * 2;
				else if (width == maxWidth && height < maxHeight &&
					this.horizontalScrollBar)
					height+=this.horizontalScrollBar.height * 2;
			}
			center();
			menuSupport.createMenuItem('关闭', onClose, false, true, 0);
		}

		//导致重影，无法解决
		private function followMouse():void
		{
			trace(mouseX, mouseY);
			var moveToX:Number=mouseX - width / 2;
			var moveToY:Number=mouseY - height / 2;

			if (moveToX < 0)
				moveToX=0;

			if (moveToX + width > stage.stageWidth)
				moveToX=stage.stageWidth - width;

			if (moveToY + height > stage.stageHeight)
				moveToY=stage.stageHeight - height;

			//顺序不能改变，保证关闭按钮显示
			if (moveToY < 0)
				moveToY=0;
			x=moveToX;
			y=moveToY;
		}

		private function onClose(evt:Event=null):void
		{
			closeProgress();

			if (this.isPopUp)
				PopUpManager.removePopUp(this);
		}

		private function onFocus(e:Event):void
		{
			if (e.target == this)
			{
				trace('PopWindow onFocus:', this, e);
				setFocus();
			}
		}

		private function onKeyDown(evt:KeyboardEvent):void
		{
			trace(this.className, evt.target, evt)
			if (evt.keyCode == Keyboard.ESCAPE)
				close();
		}

		/**
		 * 必须设置explicitWidth和explicitHeight，
		 * 如果设置width和height，窗口将不能随内容动态扩展
		 * @param e
		 */
		private function switchSize(e:Event):void
		{
			if (isNaN(originX))
			{
				originWidth=explicitWidth;
				originHeight=explicitHeight;
			}

			if (isNaN(explicitWidth) || isNaN(explicitHeight) ||
				(originWidth == explicitWidth && originHeight == explicitHeight))
			{
				explicitWidth=parent.width;
				explicitHeight=parent.height;
				originX=x;
				originY=y;
				x=0;
				y=0;
			}
			else
			{
				explicitWidth=originWidth;
				explicitHeight=originHeight;
				x=originX;
				y=originY;
			}
			trace(width, height, explicitWidth, explicitHeight);
		}
	}
}

