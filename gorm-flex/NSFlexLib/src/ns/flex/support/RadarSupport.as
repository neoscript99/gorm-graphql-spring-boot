package ns.flex.support
{

import flash.events.ContextMenuEvent;
import flash.events.Event;

import fr.kapit.radarchart.RadarChartAxis;

import mx.controls.dataGridClasses.DataGridColumn;

import ns.flex.controls.DataGridColumnPlus;

import ns.flex.controls.DataGridPlus;
import ns.flex.controls.radar.DataGridRadar;
import ns.flex.controls.radar.DataGridRadarSelector;

public class RadarSupport
{
    private var dgp:DataGridPlus;
    private var selector:DataGridRadarSelector = new DataGridRadarSelector();
    private var radarPop:DataGridRadar = new DataGridRadar();

    public function RadarSupport(dgp:DataGridPlus)
    {
        this.dgp = dgp;
    }

    public function init():void
    {
        dgp.addMenuAfterCURD('加入雷达图(可多选)', addForRadar, true);
        selector.addEventListener('showRadar', showRadar);
        selector.labelFunction = defaultLabelFunction;
    }

    public function defaultLabelFunction(item:Object):String
    {
        for each(var col:DataGridColumn in dgp.columns)
        {
            if (col.headerText != '#')
                return col.itemToLabel(item)
        }
        return '';
    }

    private function addForRadar(evt:ContextMenuEvent):void
    {
        selector.addItemArray(dgp.selectedItems);
        selector.show(dgp.parent, false);
    }


    private function showRadar(evt:Event):void
    {
        selector.close();
        var axes:Array = [];
        dgp.columns.forEach(function (col:DataGridColumnPlus, index:int,
                                      array:Array):void
        {
            //作为seriesField的DataGridColumnPlus的dataField不能是嵌套属性，如type.name
            if (index == 0)
                radarPop.seriesField = col.dataField
            if (col.asNumber)
            {
                var axis:RadarChartAxis = new RadarChartAxis()
                axis.label = col.headerText
                axis.name = col.dataField
                axes.push(axis)
            }
        })
        radarPop.axes = axes;
        radarPop.dataProvider=selector.getSelectedItems()
        radarPop.show(dgp.parent);
    }
}
}
