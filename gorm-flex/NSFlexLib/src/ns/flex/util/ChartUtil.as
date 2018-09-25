package ns.flex.util
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.FileReference;
	import mx.binding.utils.BindingUtils;
	import mx.charts.AxisRenderer;
	import mx.charts.CategoryAxis;
	import mx.charts.ColumnChart;
	import mx.charts.Legend;
	import mx.charts.LineChart;
	import mx.charts.PieChart;
	import mx.charts.chartClasses.ChartBase;
	import mx.charts.events.ChartItemEvent;
	import mx.charts.series.ColumnSeries;
	import mx.charts.series.LineSeries;
	import mx.charts.series.PieSeries;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.LinkButton;
	import mx.graphics.ImageSnapshot;
	import mx.rpc.remoting.mxml.Operation;
	import ns.flex.controls.PopWindow;
	
	/**
	 * 图形显示工具类
	 * @author wangchu
	 */
	public class ChartUtil
	{
		/**
		 * 产生饼图
		 * @param chartName 标题
		 * @param parent 父显示对象
		 * @param chartData 原数据
		 * @param field 值字段
		 * @param nameField 名称字段
		 * @param itemClick 点击时的回调函数
		 * @param width 初始宽
		 * @param height 初始高
		 */
		static public function showPie(chartName:String, parent:DisplayObject,
			chartData:*, field:String='value', nameField:String='name',
			itemClick:Function=null, width:int=480, height:int=400):PopWindow
		{
			var pc:PieChart=new PieChart();
			pc.dataProvider=chartData;
			pc.showDataTips=true;
			BindingUtils.bindProperty(pc, 'width', pc, 'height');
			pc.percentHeight=100;
			
			if (itemClick != null)
				pc.addEventListener(ChartItemEvent.ITEM_CLICK, itemClick);
			var ps:PieSeries=new PieSeries();
			ps.field=field;
			ps.nameField=nameField;
			ps.setStyle('labelPosition', 'inside');
			ps.labelFunction=LabelUtil.getPieSeriesLabel;
			pc.series=[ps];
			var legend:Legend=new Legend();
			legend.dataProvider=pc;
			return showChart(chartName, parent, pc, legend, width, height);
		}
		
		/**
		 * 曲线图
		 * @param chartName 标题
		 * @param parent 父显示对象
		 * @param operation 提供数据的远程对象方法
		 * @param categoryField x轴字段
		 * @param series y轴字段序列
		 * @param itemClick 点击回调函数
		 * @param width 宽
		 * @param height 高
		 */
		static public function showLine(chartName:String, parent:DisplayObject,
			operation:Operation, categoryField:String, series:Array, itemClick:Function=
			null, width:int=700, height:int=400):PopWindow
		{
			var lineChart:LineChart=new LineChart();
			//lineChart.dataProvider=chartData;
			BindingUtils.bindProperty(lineChart, 'dataProvider', operation, 'lastResult');
			lineChart.percentWidth=100;
			lineChart.percentHeight=100;
			lineChart.showDataTips=true;
			
			if (itemClick != null)
				lineChart.addEventListener(ChartItemEvent.ITEM_CLICK, itemClick);
			var axis:CategoryAxis=new CategoryAxis();
			axis.categoryField=categoryField;
			axis.ticksBetweenLabels=false;
			BindingUtils.bindProperty(axis, 'dataProvider', operation, 'lastResult');
			lineChart.horizontalAxis=axis;
			var axisRenderer:AxisRenderer=new AxisRenderer;
			axisRenderer.axis=axis;
			axisRenderer.setStyle("canDropLabels", true);
			lineChart.horizontalAxisRenderers=[axisRenderer];
			
			for each (var s:Object in series)
			{
				var ls:LineSeries=new LineSeries();
				ls.yField=s.yField;
				ls.displayName=s.displayName;
				//忽略miss数据，曲线连续
				ls.interpolateValues=true;
				ls.setStyle('form', 'curve');
				lineChart.series.push(ls);
			}
			var legend:Legend=new Legend();
			legend.dataProvider=lineChart;
			return showChart(chartName, parent, lineChart, legend, width, height);
		}
		
		/**
		 * 列图
		 * @param chartName 标题
		 * @param parent 父显示对象
		 * @param chartData 绑定数据源
		 * @param categoryField x轴字段
		 * @param series y轴字段序列
		 * @param itemClick 点击回调函数
		 * @param width 宽
		 * @param height 高
		 */
		static public function showColumn(chartName:String, parent:DisplayObject,
			chartData:*, categoryField:String, series:Array, itemClick:Function=null,
			width:int=700, height:int=400):PopWindow
		{
			var columnChart:ColumnChart=new ColumnChart();
			columnChart.dataProvider=chartData;
			columnChart.showDataTips=true;
			columnChart.percentWidth=100;
			columnChart.percentHeight=100;
			
			if (itemClick != null)
				columnChart.addEventListener(ChartItemEvent.ITEM_CLICK, itemClick);
			var axis:CategoryAxis=new CategoryAxis();
			axis.categoryField=categoryField;
			columnChart.horizontalAxis=axis;
			
			for each (var s:Object in series)
			{
				var cs:ColumnSeries=new ColumnSeries()
				cs.xField=s.xField;
				cs.yField=s.yField;
				cs.displayName=s.displayName;
				cs.labelFunction=LabelUtil.getColumnSeriesLabel;
				cs.setStyle('labelPosition', 'inside');
				columnChart.series.push(cs);
			}
			var legend:Legend=new Legend();
			legend.dataProvider=columnChart;
			return showChart(chartName, parent, columnChart, legend, width, height);
		}
		
		static public function showChart(chartName:String, parent:DisplayObject,
			chart:ChartBase, legend:Legend, width:int, height:int):PopWindow
		{
			var hbox:HBox=new HBox;
			hbox.percentHeight=hbox.percentWidth=100;
			hbox.setStyle('horizontalAlign', 'center');
			var vbox:VBox=new VBox();
			vbox.percentHeight=100;
			vbox.setStyle('horizontalAlign', 'right');
			var legendBox:HBox=new HBox;
			legendBox.percentHeight=100;
			legendBox.addChild(legend);
			var link:LinkButton=new LinkButton();
			link.label='保存';
			link.addEventListener(MouseEvent.CLICK, function(e:Event):void
			{
				link.visible=false;
				new FileReference().save(ImageSnapshot.captureImage(hbox).data,
					chartName + '.png');
				link.visible=true;
			});
			
			with (ContainerUtil)
			{
				builderContainer(vbox, legendBox, link);
				return showPopUP(chartName, parent, builderContainer(hbox, chart, vbox),
					width, height);
			}
		}
	}
}