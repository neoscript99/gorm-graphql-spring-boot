package ns.flex.util
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import ns.flex.common.Messages;
	import ns.flex.controls.ProgressBox;

	public class DictManager
	{
		private static var _dictionaryService:RemoteObject;

		public static function get dictionaryService():RemoteObject
		{
			return _dictionaryService
		}
		private static var _initCompleted:Boolean=false;

		public static function get initCompleted():Boolean
		{
			return _initCompleted
		}
		private static var listMap:Object={};
		private static var pb:ProgressBox=new ProgressBox()
		private static var readyCount:int=0;

		public static function getLabel(operationName:String, fieldName:String,
			fieldValue:Object, labelField:String):String
		{
			var oo:Object=getList(operationName).findByField(fieldName, fieldValue);
			return oo ? oo[labelField] : String(fieldValue);
		}

		public static function getList(operationName:String):ArrayCollectionPlus
		{
			return getListFromMap(operationName);
		}

		public static function getListWithAll(operationName:String,
			labelField:String):ArrayCollectionPlus
		{
			return getListFromMap(operationName, labelField, true);
		}

		public static function getListWithAskToChoose(operationName:String,
			labelField:String):ArrayCollectionPlus
		{
			return getListFromMap(operationName, labelField, false, true);
		}

		public static function getResult(operationName:String):Object
		{
			return _dictionaryService.getOperation(operationName).lastResult;
		}

		public static function init(ds:RemoteObject, readyGoal:int,
			parent:DisplayObject):void
		{
			_dictionaryService=ds;
			pb.title='System Initializing...';
			pb.show(parent);
			_dictionaryService.addEventListener(ResultEvent.RESULT, function(e:Event):void
			{
				if ((++readyCount) >= readyGoal)
				{
					_initCompleted=true;
					pb.close();
				}
			});
		}

		private static function getListFromMap(operationName:String,
			labelField:String=null, withAll:Boolean=false,
			withAskToChoose:Boolean=false):ArrayCollectionPlus
		{
			var key:String=operationName.concat(withAll, withAskToChoose);
			if (!listMap[operationName])
			{
				if (labelField && (withAll || withAskToChoose))
				{
					var first:Object={};
					first[labelField]=withAll ? Messages.ALL : Messages.ASK_TO_CHOOSE;
					listMap[key]=
						ArrayCollectionPlus.withFirst(_dictionaryService.getOperation(operationName).lastResult,
						first);
				}
				else
					listMap[key]=
						new ArrayCollectionPlus(_dictionaryService.getOperation(operationName).lastResult);

			}
			return listMap[key];
		}
	}
}

