package ns.gflex.I18n
{
	import flash.utils.getDefinitionByName;

	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;

	import ns.flex.util.Session;

	public class I18n
	{
		/*
		   It turns out the reason why the error was being thrown was that
		   in my Flex Application I didn't have anything which "used" the class. ' +
		   'In other words the compiler thought that the class was left over junk and didn't include it in the compiled swf. By adding in a use of it,
		   everything started working. As example use would be something like:
		   http://life.neophi.com/danielr/2006/07/flex_2_runtime_error_1065.html
		 */
		static private var _map:Object;

		static public function get map():Object
		{
			if (!_map)
			{
				//定义两个变量使编译器编译这两个类，从而可以动态构建
				var dualChinese:I18n_Chinese=null;
				var dualEnglish:I18n_English=null;
				var qName:String=
					'ns.gflex.I18n.I18n_' + Session['ParamMap'].I18nLanguage;
				var i18n:Class;

				try
				{
					i18n=getDefinitionByName(qName) as Class;
				}
				catch (e:Error)
				{
					trace("<Error> " + e.message);
				}
				_map=i18n ? new i18n() : new I18n_Chinese();
			}
			return _map;
		}

		/**
		 *
		 * @param name
		 * @return
		 */
		static public function get(name:String):String
		{
			return map[name] ? map[name] : name;
		}

		static public function parse(str:String):String
		{
			//replace each ${name} with get(name)
			str.match(/\$\{([^\$]+)\}/g).forEach(function(item:*, index:int,
					array:Array):void
					{
						str=str.replace(item, get(item.replace(/[\$\{\}]/g, '')));
					});
			return str;
		}

		static public function showError(e:FaultEvent, title:String="Warning"):void
		{
			var message:String=
				e.fault.faultString.substr(e.fault.faultString.indexOf(':') + 2);
			Alert.show(parse(message), title);
		}

		static public function showMessage(messageName:String, titleName:String=
			'common.message.title'):void
		{
			Alert.show(get(messageName), get(titleName));
		}

		/**
		 *
		 * @param name
		 * @param params
		 * @return
		 */
		static public function getMessage(name:String, params:Array):String
		{
			var message:String=get(name);

			if (message)
				for (var i:int=1; i <= params.length; i++)
					message=message.replace(String('{').concat(i, '}'), params[i - 1]);
			return message;
		}
	}
}

