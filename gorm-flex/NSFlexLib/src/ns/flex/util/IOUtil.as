package ns.flex.util
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.net.FileReference;
	import flash.utils.ByteArray;
	import mx.controls.Alert;

	public class IOUtil
	{
		/**
		 *
		 * @param byteSize
		 * @return xxGB MB KB B
		 */
		public static function getFileSize(byteSize:Number):String
		{
			if (byteSize > 1024 * 1024 * 1024)
				return String(Number(byteSize / 1024 / 1024 / 1024).toFixed(2)).concat('GB');
			else if (byteSize > 1024 * 1024)
				return String(Number(byteSize / 1024 / 1024).toFixed(2)).concat('MB');
			else if (byteSize > 1024)
				return String(Number(byteSize / 1024).toFixed(2)).concat('KB');
			else
				return String(byteSize).concat('B');
		}

		public static function loadFile(loadComplete:Function,
			fileFilters:Array=null):void
		{
			var fileReference:FileReference=new FileReference();
			fileReference.addEventListener(Event.SELECT, function(e:Event):void
			{
				fileReference.addEventListener(Event.COMPLETE, loadComplete);
				fileReference.load();
			});
			fileReference.browse(fileFilters);
		}

		public static function saveFile(data:ByteArray, fileName:String):void
		{
			new FileReference().save(data, fileName);
		}

		/**
		 * Flash安全原因，保存文件必须通过点击按钮等事件触发，如果不符合可通过这个方法实现
		 * @param data
		 * @param fileName
		 * @param parent
		 */
		public static function saveFileWithAlert(data:ByteArray, fileName:String,
			parent:Sprite, title:String='下载完成,请保存'):void
		{
			Alert.show(title, null, Alert.OK, parent, function(evt:Event):void
			{
				new FileReference().save(data, fileName);
			})
		}
	}
}

