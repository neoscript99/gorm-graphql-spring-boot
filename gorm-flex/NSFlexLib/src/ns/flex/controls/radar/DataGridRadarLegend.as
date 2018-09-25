package ns.flex.controls.radar
{
import flash.text.TextField;
import flash.text.TextFieldAutoSize;

import fr.kapit.radarchart.RadarChartSeriesLegend;

import mx.core.UIComponent;

public class DataGridRadarLegend extends RadarChartSeriesLegend
        implements fr.kapit.radarchart.IRadarChartLegend
{
    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        for each(var child:UIComponent in this.getChildren())
        {
            var textField:TextField = child.getChildAt(0) as TextField;
            textField.multiline = true;
            textField.wordWrap = true;
            textField.autoSize = TextFieldAutoSize.CENTER;
        }
    }
}
}
