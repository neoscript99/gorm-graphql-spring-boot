package ns.flex.util
{
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;
	
	public class NetUtil
	{
		static public function postUrl(url:String, params:Object=null, target:String=
			null):void
		{
			var request:URLRequest=new URLRequest(url);
			var variables:URLVariables=new URLVariables();
			
			for (var prop:* in params)
				variables[prop]=params[prop];
			variables.passport='ooajUUdj233LL084MMjjjell';
			request.data=variables;
			request.method=URLRequestMethod.POST;
			navigateToURL(request, target);
		}
	}
}