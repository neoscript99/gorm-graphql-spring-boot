package ns.flex.util
{
import mx.charts.ChartItem;
import mx.charts.chartClasses.Series;
import mx.charts.series.items.ColumnSeriesItem;
import mx.controls.dataGridClasses.DataGridColumn;

public class LabelUtil
{
    static public function getPieSeriesLabel(data:Object, field:String, index:Number,
                                             percentValue:Number):String
    {
        return StringUtil.formatNumber(data[field], 0).concat('(',
                StringUtil.formatNumber(percentValue, 0, false), '%', ')');
    }

    static public function getColumnSeriesLabel(element:ChartItem,
                                                series:Series):String
    {
        var item:ColumnSeriesItem = ColumnSeriesItem(element);
        return StringUtil.formatNumber(item.yNumber, 0);
    }

    static public function booleanLabel(item:Object, column:DataGridColumn):String
    {
        return item[column.dataField] ? '是' : '否';
    }
}
}