package ns.flex.util
{
	import flash.utils.ByteArray;

	public class ObjectUtils
	{

		public static function clone(source:Object):*
		{
			var myBA:ByteArray=new ByteArray();
			myBA.writeObject(source);
			myBA.position=0;
			return (myBA.readObject());
		}

		public static function copyProperties(toObject:Object, fromObject:Object,
			isDynamic:Boolean=false):void
		{
			if (fromObject)
				for (var prop:String in fromObject)
					if ((toObject.hasOwnProperty(prop) || isDynamic) &&
						prop != 'mx_internal_uid') //mx_internal_uid是内部识别码，复制会有问题
						toObject[prop]=fromObject[prop];
		}

		public static function getValue(obj:Object, field:String):Object
		{
			var cur:Object=obj;
			var chain:Array=field.split('.');
			for (var i:int=0; i < chain.length; i++)
				if (cur && cur.hasOwnProperty(chain[i]))
					cur=cur[chain[i]];
				else
					return null;
			return cur;
		}

		public static function mergeObject(... objs):Object
		{
			return mergeObjectArray(objs);
		}

		public static function mergeObjectArray(objs:Array):Object
		{
			var merger:Object={};

			for each (var obj:* in objs)
				copyProperties(merger, obj, true);
			return merger;
		}

		/**
		 * 给对象属性赋值，field可以为类似same.name的复杂嵌套字段
		 * @param obj
		 * @param field
		 * @param value
		 */
		public static function setValue(obj:Object, field:String, value:*):void
		{
			var nestItem:Object=obj;
			field.split('.').forEach(function(element:*, index:int, arr:Array):void
			{
				if (index < arr.length - 1)
				{
					if (!nestItem[element])
						nestItem[element]={}
					nestItem=nestItem[element]
				}
				else
					nestItem[element]=value
			})
		}
	}
}

