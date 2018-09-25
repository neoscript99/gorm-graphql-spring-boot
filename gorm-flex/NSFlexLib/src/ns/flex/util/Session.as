package ns.flex.util
{

	public dynamic class Session
	{
		static private var map:Object={};

		static public function get(name:String):Object
		{
			return map[name];
		}

		static public function set(name:String, value:Object):Object
		{
			return map[name]=value;
		}
	}
}

