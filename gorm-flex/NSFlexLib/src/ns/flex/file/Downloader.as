package ns.flex.file
{
	import mx.containers.VBox;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import ns.flex.util.ArrayUtil;
	import ns.flex.util.RemoteUtil;

	public class Downloader extends VBox
	{

		[Bindable]
		public var deletable:Boolean=false;

		[Bindable]
		public var service:RemoteObject;
		private var _ownerId:String;

		public function Downloader(destination:String=null)
		{
			super();
			if (destination)
				this.destination=destination;
		}

		public function set dataProdiver(list:*):void
		{
			removeAllChildren();
			for each (var fileInfo:Object in ArrayUtil.toArray(list))
			{
				var fileItem:FileItem=new FileItem;
				fileItem.service=service;
				fileItem.fileId=fileInfo.fileId;
				fileItem.fileName=fileInfo.name;
				fileItem.fileSize=fileInfo.fileSize;
				fileItem.dateCreated=fileInfo.dateCreated;
				fileItem.deletable=deletable;
				addChild(fileItem);
			}
		}

		/**
		 * 赋值后查询，如果不用查询，直接设置service
		 * @param destination
		 */
		public function set destination(destination:String):void
		{
			service=RemoteUtil.createRemoteObject(destination, null, null, true);
			service.getOperation('queryAttachByOwner').addEventListener(ResultEvent.RESULT,
				function(e:ResultEvent):void
			{
				dataProdiver=e.result;
			});
			if (_ownerId)
				service.queryAttachByOwner(_ownerId);
		}

		public function set ownerId(ownerId:String):void
		{
			_ownerId=ownerId;
			if (service && _ownerId)
				service.queryAttachByOwner(_ownerId);
		}
	}
}
