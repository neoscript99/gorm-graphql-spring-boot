package ns.flex.controls
{
	import mx.controls.Label;

	/**
	 * datagrid数据展现render类，改为text可以复制
	 * @author wangchu
	 */
	public class SelectableLabel extends Label
	{
		public function SelectableLabel()
		{
			super();
			selectable=true;
		}
	}
}

