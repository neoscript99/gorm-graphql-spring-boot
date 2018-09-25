package ns.flex.report
{
import flash.events.Event;
import flash.ui.ContextMenuItem;

import mx.containers.Panel;
import mx.events.FlexEvent;
import mx.events.ListEvent;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import ns.flex.controls.DataGridPlus;
import ns.flex.controls.PagerBase;
import ns.flex.controls.PagerSmart;
import ns.flex.controls.Paging;
import ns.flex.controls.ProgressBox;
import ns.flex.module.AbstractModule;
import ns.flex.util.ContainerUtil;
import ns.flex.util.RemoteUtil;
import ns.flex.util.SQLUtil;
import ns.flex.util.Session;

public class ReportModule extends AbstractModule
{
    public var destination:String;
    [Bindable]
    public var drillDepth:int = 0;

    [Inspectable(category="General")]
    [Bindable]
    public var drillable:Boolean = false;

    /**
     * 后台是否已有默认domain，如果有调用远程方法时无需送domain参数
     */
    [Inspectable(category="General")]
    [Bindable]
    public var hasDefaultDomain:Boolean = false;
    protected var drillLast:Object;
    protected var popProgress:ProgressBox = new ProgressBox;
    [Bindable]
    protected var reportService:RemoteObject;
    protected var sessionUserChange:Boolean = false;
    private var drillHist:Array = [];
    private var drillPageHist:Array = [];
    private var sessionUser:Object;

    /**
     * 必须在继承的类中初始化reportService
     */
    public function ReportModule()
    {
        addEventListener(FlexEvent.CREATION_COMPLETE, cc);
    }

    override public function beforeDisplay():void
    {
        //如果第一次登录
        if (!sessionUser)
            sessionUser = Session['LoginUser'];
        //如果用户已存在但不同于新用户
        else if (sessionUser && sessionUser != Session['LoginUser'])
        {
            sessionUser = Session['LoginUser'];
            sessionUserChange = true;
            invalidateProperties();
        }
    }

    public function query(first:int = -1):void
    {
        if (paging)
            queryPage(first)
        else
            SQLUtil.list(reportService, queryParam, -1, 0, orders, domain);
    }

    public function queryPage(first:int):void
    {
        if (paging is Paging)
            SQLUtil.countAndList(reportService, queryParam, paging.offsetValue,
                    paging.forFirst(first), orders, domain);
        else if (paging is PagerSmart) //on count
            SQLUtil.list(reportService, queryParam, paging.offsetValue,
                    paging.forFirst(first), orders, domain);
    }

    protected function cc(e:Event):void
    {
        initService();
        query();
        if (drillable)
            drillInit();
    }

    override protected function commitProperties():void
    {
        super.commitProperties();
        if (sessionUserChange)
        {
            query();
            sessionUserChange = false;
        }
    }

    protected function get dgp():DataGridPlus
    {
        if (!map.dgp)
            map.dgp =
                    ContainerUtil.findContainerChild(this, DataGridPlus) as DataGridPlus;
        return map.dgp;
    }

    protected function get domain():String
    {
        if (hasDefaultDomain)
            return null;
        else
            return this.className;
    }

    protected function drillDown(e:Event):void
    {
        if (drillDepth < drillMaxDepth - 1 && dgp.selectedOriItem)
        {
            drillPageHist.push(paging ? paging.first : -1);
            if (drillLast)
                drillHist.push(drillLast);
            drillLast = drillNow;
            drillDepth++;
            query();
        }
    }

    protected function drillInit():void
    {
        var dt:ContextMenuItem = dgp.addMenuAfterCURD('返回顶层', drillTop, false, true);
        var du:ContextMenuItem = dgp.addMenuAfterCURD('向上钻取', drillUp, false, true);
        dgp.addMenuAfterCURD('向下钻取', drillDown, true);
        dgp.doubleClickEnabled = true;
        dgp.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, drillDown);
    }

    protected function get drillMaxDepth():int
    {
        return 0;
    }

    protected function get drillNow():Object
    {
        throw new Error('Please override this method')
    }

    protected function drillTop(e:Event):void
    {
        drillDepth = 0;
        drillLast = null;
        drillHist = [];
        query(drillPageHist[0]);
        drillPageHist = []
    }

    protected function drillUp(e:Event):void
    {
        if (drillDepth > 0)
        {
            drillLast = drillHist.pop();
            drillDepth--;
            query(drillPageHist.pop());
        }
    }

    protected function export():void
    {
        popProgress.show(this)
        reportService.export(exportParam, domain);
    }

    protected function exportFile(evt:Event):void
    {
        popProgress.close()
        dgp.saveAsExcelWithAlert(reportService.export.lastResult, exportFileName)
    }

    protected function get exportFileName():String
    {

        var panel:Panel = ContainerUtil.findParent(this, Panel) as Panel;
        return panel ? panel.title : name;
    }

    protected function get exportParam():Object
    {
        return queryParam;
    }

    protected function initService():void
    {
        if (destination)
            reportService = RemoteUtil.createRemoteObject(destination);
        reportService.addEventListener(FaultEvent.FAULT, function (e:FaultEvent):void
        {
            popProgress.close();
            dgp.closeProgress();
        });
        reportService.getOperation('save').addEventListener(ResultEvent.RESULT,
                function (e:ResultEvent):void
                {
                    dgp.closePop();
                    refresh();
                });
        reportService.getOperation('deleteByIds').addEventListener(ResultEvent.RESULT,
                refresh);
        reportService.getOperation('deleteByStringList').addEventListener(ResultEvent.RESULT,
                refresh);
        reportService.getOperation('deleteByNumberList').addEventListener(ResultEvent.RESULT,
                refresh);
        reportService.getOperation('export').addEventListener(ResultEvent.RESULT,
                exportFile);
    }

    protected function get item():Object
    {
        return dgp.selectedOriItem;
    }

    protected function get orders():Array
    {
        return dgp.orders;
    }

    protected function get paging():PagerBase
    {
        if (!map.paging)
            map.paging = ContainerUtil.findContainerChild(this, PagerBase);
        return map.paging;
    }

    protected function get queryParam():Object
    {
        return reportBar ? reportBar.queryParam : null;
    }

    protected function refresh(e:Event = null):void
    {
        if (paging)
            paging.refresh();
        else
            query();
    }

    protected function get reportBar():ReportControlBar
    {
        if (!map.reportBar)
            map.reportBar = ContainerUtil.findContainerChild(this, ReportControlBar);
        return map.reportBar;
    }

    protected function get selectedIds():Array
    {
        return dgp.getFieldArray('id')
    }
}
}

