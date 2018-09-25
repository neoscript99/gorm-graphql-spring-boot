package ns.flex.controls
{
import com.as3xls.xls.Cell;
import com.as3xls.xls.ExcelFile;
import com.as3xls.xls.Sheet;

import flash.events.ContextMenuEvent;
import flash.events.Event;
import flash.net.FileFilter;
import flash.system.System;
import flash.ui.ContextMenuItem;
import flash.utils.ByteArray;

import mx.collections.IList;
import mx.containers.ControlBar;
import mx.containers.Form;
import mx.controls.DataGrid;
import mx.controls.dataGridClasses.DataGridColumn;
import mx.events.DataGridEvent;
import mx.events.FlexEvent;
import mx.events.ListEvent;
import mx.managers.PopUpManager;
import mx.utils.ObjectProxy;
import mx.utils.ObjectUtil;
import mx.utils.UIDUtil;

import ns.flex.common.Constants;
import ns.flex.controls.radar.DataGridRadar;
import ns.flex.event.MultCreateEvent;
import ns.flex.event.SaveItemEvent;
import ns.flex.event.SaveMultItemEvent;
import ns.flex.event.ShowItemEvent;
import ns.flex.popup.PopMultInput;
import ns.flex.support.MenuSupport;
import ns.flex.support.RadarSupport;
import ns.flex.util.ArrayCollectionPlus;
import ns.flex.util.ComponentUtil;
import ns.flex.util.ContainerUtil;
import ns.flex.util.IOUtil;
import ns.flex.util.MathUtil;
import ns.flex.util.MessageUtil;
import ns.flex.util.ObjectUtils;
import ns.flex.util.StringUtil;

[Event(name="createItem")]
[Event(name="showItem", type="ns.flex.event.ShowItemEvent")]
[Event(name="saveItem", type="ns.flex.event.SaveItemEvent")]
[Event(name="saveMultItem", type="ns.flex.event.SaveMultItemEvent")]
[Event(name="modifyItem")]
[Event(name="deleteItems")]
[Event(name="deleteAll")]
[Event(name="changeOrder")]
[Event(name="multCreate", type="ns.flex.event.MultCreateEvent")]
public class DataGridPlus extends DataGrid
{
    public static const CLONE_KEY:String = 'ObjectCreateByClone';

    public var appBar:AppBarPlus;
    [Inspectable(category="General")]
    public var appBarSearchEnabled:Boolean = false;

    [Inspectable(category="General")]
    public var cloneEnabled:Boolean = false;

    [Inspectable(category="General")]
    public var copyToExcelEnabled:Boolean = true;
    [Inspectable(category="General")]
    public var createEnabled:Boolean = false;
    [Inspectable(enumeration="asc,desc", defaultValue="desc", category="General")]
    public var defaultOrder:String = 'desc';
    [Inspectable(category="General")]
    public var deleteAllEnabled:Boolean = false;
    [Inspectable(category="General")]
    public var deleteEnabled:Boolean = false;
    [Inspectable(category="General")]
    public var exportDataField:Boolean = false;
    public var exportName:String;
    [Inspectable(category="General")]
    public var exportNumber:Boolean = false;
    [Inspectable(category="General")]
    public var globalSort:Boolean = false;
    public var menuSupport:MenuSupport;
    [Inspectable(category="General")]
    public var modifyEnabled:Boolean = false;
    [Inspectable(category="General")]
    public var multCreateEnabled:Boolean = false;
    [Inspectable(category="General")]
    public var multEditEnabled:Boolean = false;
    [Inspectable(category="General")]
    public var multiDelete:Boolean = true;
    //如果有嵌套字段，排序顺序无法保证，不要使用
    [Inspectable(category="General")]
    public var multiSort:Boolean = false;
    [Inspectable(category="General")]
    public var openExcelEnabled:Boolean = false;
    public var popEditing:PopWindow;
    [Inspectable(category="General")]
    public var popEnterSubmit:Boolean = true;
    public var popMultEditing:PopWindow;
    public var popTitleFunciton:Function;
    public var popView:PopWindow;
    //自动生成的popView和popEditing是否重用，如果字段稳定，应该重用
    [Inspectable(category="General")]
    public var reusePop:Boolean = true;
    [Inspectable(enumeration="none,new,view,edit,new-view,new-edit,view-edit,new-view-edit",
            defaultValue="none", category="General")]
    public var showDetail:String = 'none';
    [Inspectable(category="General")]
    public var showIndex:Boolean = true;
    [Bindable]
    public var showItemProxy:ObjectProxy = new ObjectProxy();
    public var newItemTemplate:Object;
    [Inspectable(category="General")]
    public var showOnlyVisible:Boolean = true;
    [Inspectable(category="General")]
    public var showSum:Boolean = false;
    public var sumColumnLabel:String = '◆汇总◆';
    [Inspectable(category="General")]
    public var showRadar:Boolean = false;

    /**
     * 根据指向的item，判断当前菜单能否点击
     * function(menu:ContextMenuItem,item:Object):Boolean
     * @default
     */
    private var _menuEnableChecker:Function;
    private var _showItem:Object;
    private var curdMenuPosition:int;
    private var exportMenuPosition:int;
    private var indexColumn:DataGridColumnPlus;
    [Bindable]
    private var lastRollOverIndex:Number;
    private var multCreateInput:PopMultInput;
    private var orderList:ArrayCollectionPlus = new ArrayCollectionPlus();
    private var replacableDoubleClickHandler:Function;
    private var radarSupport:RadarSupport;

    public function DataGridPlus()
    {
        super();
        allowMultipleSelection = true;
        toolTip = '右键菜单更多功能';
        //variableRowHeight为true后，再设置rowCount，得到的最终rowCount可能不准确
        //height=第一行rowHeignt*rowCount
        variableRowHeight = true;
        addEventListener(ListEvent.ITEM_ROLL_OVER, dgItemRollOver);
        addEventListener(ListEvent.ITEM_ROLL_OUT, dgItemRollOut);
        addEventListener(FlexEvent.INITIALIZE, init);
        addEventListener(DataGridEvent.HEADER_RELEASE, onHeaderRelease);

        addEventListener('resetEditItem', function (e:Event):void
        {
            showItemProxy = new ObjectProxy(ObjectUtil.copy(_showItem));
        });
    }

    public static function getCleanHeader(col:DataGridColumn):String
    {
        return col.headerText ? col.headerText.replace(/[↑↓]\d*/g, '') : '';
    }

    override public function addEventListener(type:String, listener:Function,
                                              useCapture:Boolean = false, priority:int = 0, useWeakReference:Boolean = false):void
    {
        if (type == ListEvent.ITEM_DOUBLE_CLICK &&
                replacableDoubleClickHandler != null)
        {
            this.removeEventListener(ListEvent.ITEM_DOUBLE_CLICK,
                    replacableDoubleClickHandler);
            replacableDoubleClickHandler = null;
        }
        super.addEventListener(type, listener, useCapture, priority, useWeakReference);
    }

    public function addMenuAfterCURD(caption:String, listener:Function,
                                     separatorBefore:Boolean = false, alwaysEnabled:Boolean = false,
                                     position:int = 0):ContextMenuItem
    {
        return menuSupport.createMenuItem(caption, listener, separatorBefore,
                alwaysEnabled, curdMenuPosition + position);
    }

    public function addMenuAfterExport(caption:String, listener:Function,
                                       separatorBefore:Boolean = false, alwaysEnabled:Boolean = false,
                                       position:int = 0):ContextMenuItem
    {
        return menuSupport.createMenuItem(caption, listener, separatorBefore,
                alwaysEnabled, exportMenuPosition + position);
    }

    public function addOrder(sortField:String, order:String = null):void
    {
        var item:Object;

        for (var i:int = 0; i < orderList.length; i++)
            if (orderList[i].sortField == sortField)
            {
                item = orderList[i];
                item.order =
                        order == null ? (item.order == 'asc' ? 'desc' : 'asc') : order;
                orderList.removeItemAt(i);
                break;
            }

        //是否可以多列排序
        if (!multiSort)
            orderList.removeAll();

        if (item == null)
            item = {sortField: sortField, order: order == null ? defaultOrder : order};
        orderList.addFirst(item);
        refreshHeadText();
    }

    public function closePop():void
    {
        if (popEditing)
            popEditing.close();
        if (popMultEditing)
            popMultEditing.close();

        if (popView)
            popView.close();
    }

    public function closeProgress():void
    {
        if (popEditing)
            popEditing.closeProgress();
        if (popMultEditing)
            popMultEditing.closeProgress();
    }

    [Inspectable(category="Data", defaultValue="undefined")]
    override public function set dataProvider(value:Object):void
    {
        trace('set dataProvider');
        if (showSum && value && value.length > 1)
        {
            //clone source
            var acp:ArrayCollectionPlus = new ArrayCollectionPlus(value);
            var sumItem:Object = {uniqueIdForSumItem: uid};
            var hasGroupColumn:Boolean = false;
            if (acp.length > 0)
            {
                var minVisible:int = -1;
                var cols:Array = columns;
                for (var ci:int = 0; ci < cols.length; ci++)
                {
                    var col:DataGridColumn = cols[ci];
                    if (col.dataField)
                    {
                        if (minVisible < 0 && col.visible)
                            minVisible = ci;
                        var genValue:Object = ''
                        if (col is DataGridColumnPlus && col['groupMethod'] &&
                                col['groupMethod'] != 'none')
                        {
                            hasGroupColumn = true;
                            var valueArray:Array = [];
                            for (var i:int = 0; i < acp.length; i++)
                                valueArray.push((col as DataGridColumnPlus).getValue(acp[i]));
                            genValue = MathUtil[col['groupMethod']](valueArray);
                        }
                        else if (minVisible == ci)
                            genValue = sumColumnLabel
                        ObjectUtils.setValue(sumItem, col.dataField, genValue);
                    }
                }
                if (hasGroupColumn)
                    acp.addItem(sumItem);
                super.dataProvider = acp;
                return;
            }
        }
        super.dataProvider = value;
    }

    public function get editableColumns():Array
    {
        return (showOnlyVisible ? visibleColumns : columns).filter(function (item:DataGridColumn,
                                                                             index:int, array:Array):Boolean
        {
            return item.editable;
        })
    }

    public function getFieldArray(field:String, all:Boolean = false):Array
    {
        return new ArrayCollectionPlus(all ? dataProvider : selectedItems).getFieldArray(field)
    }

    public function initPopEditing():PopWindow
    {
        if (popEditing && reusePop)
            return popEditing;
        popEditing = initPop(true);
        return popEditing;
    }

    public function initPopMultEditing():PopWindow
    {
        if (popMultEditing && reusePop)
            return popMultEditing;
        popMultEditing = initPop(true, true);
        return popMultEditing;
    }

    public function initPopView():PopWindow
    {
        if (popView)
        {
            if (reusePop)
                return popView;
            else
                popView.close();
        }
        popView = initPop(false);
        return popView;
    }

    public function isSumItem(item:Object):Boolean
    {
        return item && item.uniqueIdForSumItem == uid;
    }

    public function set menuEnableChecker(fun:Function):void
    {
        _menuEnableChecker = fun;
        //防止编辑
        if (replacableDoubleClickHandler)
            doubleClickEnabled = false;
    }

    //乘数，显示万元、千元时有用
    public function set multiplier(v:Number):void
    {
        for each (var col:Object in columns)
        {
            if (col is DataGridColumnPlus)
            {
                var colp:DataGridColumnPlus = (col as DataGridColumnPlus);
                if (colp.asNumber)
                    colp.multiplier = v;
            }
        }
        invalidateSize();
    }

    public function openExcel(evt:Event = null):void
    {
        IOUtil.loadFile(function (e:Event):void
        {
            var dataArray:Array = [];
            if (e.target.data != null && e.target.data.length > 0)
            {
                var excelFile:ExcelFile = new ExcelFile();
                excelFile.loadFromByteArray(e.target.data);
                var sheet:Sheet = excelFile.sheets[0];
                for (var row:int = 1; row < sheet.rows; row++)
                {
                    var cellObject:Object = {};
                    for (var col:int = 0; col < sheet.cols; col++)
                    {
                        var cell:Cell = sheet.getCell(row, col);
                        cellObject[sheet.getCell(0, col).value] =
                                cell ? cell.value : null;
                    } // inner for loop ends
                    dataArray.push(cellObject);
                } //for loop ends
            }
            dataProvider = dataArray;
        }, [new FileFilter('Excel 97-2003(*.xls)', '*.xls')]);
    }

    public function get orders():Array
    {
        return orderList.toBiArray('sortField', 'order');
    }

    public function resetMenu():void
    {
        menuSupport = new MenuSupport(this, contextMenu_menuSelect);
        var separatorCount:int = 0;

        if (createEnabled)
            enableMenu("新增", createItem, (separatorCount++ == 0), true);
        else if (showDetail.indexOf('new') > -1)
        {
            enableMenu("新增", function (evt:Event):void
            {
                showItemDetail(newItemTemplate, true);
            }, (separatorCount++ == 0), true);
            if (cloneEnabled)
                enableMenu("克隆", function (evt:Event):void
                {
                    showItemDetail(selectedItem, true, true);
                }, (separatorCount++ == 0));
        }

        if (showDetail.indexOf('view') > -1)
            enableMenu('查看', function (evt:Event):void
            {
                showItemDetail(selectedItem, false);
            }, (separatorCount++ == 0), false, true);

        if (modifyEnabled)
            enableMenu("修改", modifyItem, (separatorCount++ == 0), false, true);
        else if (showDetail.indexOf('edit') > -1)
            enableMenu("修改", function (evt:Event):void
            {
                showItemDetail(selectedItem, true);
            }, (separatorCount++ == 0), false, true);

        if (deleteEnabled)
            enableMenu("删除选中", deleteItems, (separatorCount++ == 0));

        if (deleteAllEnabled)
            enableMenu("删除全部", deleteAll, (separatorCount++ == 0), true);

        if (multCreateEnabled)
        {
            multCreateInput = new PopMultInput;
            multCreateInput.addEventListener('confirm', multCreateConfirm);
            enableMenu("批量新增", multCreate, (separatorCount++ > 0), true);
        }

        if (multEditEnabled)
        {
            separatorCount++;
            enableMenu("批量修改", function (evt:Event):void
            {
                showItemDetail(selectedItem, true, false, true);
            }, !multCreateEnabled);
        }

        if (appBarSearchEnabled)
        {
            separatorCount++;
            enableMenu("查询相似", function (evt:Event):void
            {
                appBar.searchItem = selectedItem;
            }, true);
        }

        curdMenuPosition = exportMenuPosition = separatorCount;
        if (copyToExcelEnabled)
        {
            enableMenu("复制选择行", copySelectedToExcel, true);
            enableMenu("复制全部行", copyTotalToExcel);
            enableMenu("另存为Excel", saveToExcel);
            exportMenuPosition = curdMenuPosition + 3;
        }
        if (openExcelEnabled)
            enableMenu("打开Excel", openExcel, true, true);

        if(showRadar){
            radarSupport=new RadarSupport(this);
            radarSupport.init();
        }
    }

    public function rowsToExcel(dataList:Object):ByteArray
    {
        var cols:Array = viewableColumns;
        var sheet:Sheet = new Sheet();
        sheet.resize(dataList ? dataList.length + 1 : 1, cols.length);
        for (var k:int = 0; k < cols.length; k++)
        {
            var head:String = getCleanHeader(cols[k]);
            if (exportDataField)
                head =
                        head.concat('(',
                                cols[k].dataField ? cols[k].dataField : '@formula', ')');
            sheet.setCell(0, k, head)
        }

        if (dataList)
            for (var i:int = 0; i < dataList.length; i++)
                for (var j:int = 0; j < cols.length; j++)
                {
                    var vStr:String = StringUtil.trim(cols[j].itemToLabel(dataList[i]));
                    if (cols[j].hasOwnProperty('asNumber') && cols[j].asNumber &&
                            exportNumber)
                        vStr = vStr.replace(/,/g, '');
                    //Excel 超过10位会自动科学计数、超过15位尾数丢失，以下防止类似情况发生
                    else if (vStr.length > 10 && !isNaN(Number(vStr)))
                        vStr = '\'' + vStr + '\'';
                    sheet.setCell(i + 1, j, vStr);
                }

        var xls:ExcelFile = new ExcelFile();
        xls.sheets.addItem(sheet);

        return xls.saveToByteArray();
    }

    public function rowsToString(dataList:Object, spiltor:String = '	',
                                 withHead:Boolean = true):String
    {
        var ss:String = '';
        var cols:Array = visibleColumns;
        if (withHead)
        {
            for (var k:int = 0; k < cols.length; k++)
                ss =
                        ss.concat(StringUtil.toLine(getCleanHeader(cols[k])),
                                k == cols.length - 1 ? '' : spiltor);
            ss += '\n';
        }

        if (dataList)
            for (var i:int = 0; i < dataList.length; i++)
            {
                for (var j:int = 0; j < cols.length; j++)
                    ss =
                            ss.concat(StringUtil.toLine(cols[j].itemToLabel(dataList[i])),
                                    j == cols.length - 1 ? '' : spiltor);
                ss = ss.concat('\n');
            }

        return ss;
    }

    public function saveAsExcel(dataList:Object, fileName:String):void
    {
        IOUtil.saveFile(rowsToExcel(dataList), fileName.concat('.xls'));
    }

    public function saveAsExcelWithAlert(dataList:Object, fileName:String):void
    {
        IOUtil.saveFileWithAlert(rowsToExcel(dataList), fileName.concat('.xls'), this);
    }

    [Bindable("change")]
    [Bindable("valueCommit")]
    [Inspectable(environment="none")]

    /**
     *  An array of references to the selected items in the data provider. The
     *  items are in order same as dataProvider.
     *  @default [ ]
     */
    public function get selectedItemsInOriginOrder():Array
    {
        if (collection is IList)
        {
            var dp:IList = IList(collection);
            return selectedItems.sort(function (a:Object, b:Object):Number
            {
                return dp.getItemIndex(a) > dp.getItemIndex(b) ? 1 : -1;
            });
        }
        else
            return selectedItems
    }

    /**
     * 不选择汇总项
     * @return
     */
    public function get selectedOriItem():Object
    {
        return (selectedItem &&
                selectedItem.uniqueIdForSumItem == uid) ? null : selectedItem;
    }

    public function set showItem(o:Object):void
    {

        _showItem = o;
        showItemProxy = new ObjectProxy(ObjectUtil.copy(_showItem));
    }

    /**
     * 生成默认的详细对话框
     * @param evt
     */
    public function showItemDetail(item:Object, editable:Boolean = false,
                                   isClone:Boolean = false, isMultEdit:Boolean = false):void
    {
        //汇总行不能修改直接返回
        if (isSumItem(item) && editable)
            return;
        showItem = item;
        if (isClone)
        {
            showItemProxy.id = null;
            showItemProxy[CLONE_KEY] = true;
        }

        if (editable && isMultEdit)
        {
            initPopMultEditing();
            popMultEditing.show(root);
            popMultEditing.title = String('批量修改').concat(selectedItems.length, '条记录');
        }
        else if (editable)
        {
            initPopEditing();
            popEditing.show(root);
            if (_showItem)
                popEditing.title =
                        (isClone ? '克隆' : '修改') + (popTitleFunciton ? ' ' + popTitleFunciton(_showItem) : '');
            else
            {
                popEditing.title = '新增';
                showItemProxy.CREATE_ITEM_FLAG = true;
            }
        }
        else
        {
            initPopView()
            popView.show(root, false);
        }
        dispatchEvent(new ShowItemEvent(item, editable,
                editable ? popEditing : popView));
    }

    public function updateCMDMenu(enabled:Boolean):void
    {
        deleteEnabled = deleteAllEnabled = createEnabled = modifyEnabled = enabled;
    }

    public function get viewableColumns():Array
    {
        return (showOnlyVisible ? visibleColumns : columns).filter(function (item:DataGridColumn,
                                                                             index:int, array:Array):Boolean
        {
            return (item is DataGridColumnPlus && DataGridColumnPlus(item).viewable) ||
                    !(item is DataGridColumnPlus);
        })
    }

    public function get visibleColumns():Array
    {
        return columns.filter(function (item:DataGridColumn, index:int,
                                        array:Array):Boolean
        {
            return item.visible && item != indexColumn;
        })
    }

    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight)
        if (indexColumn && indexColumn.width > 30)
            indexColumn.width = 30
    }

    private function contextMenu_menuSelect(evt:ContextMenuEvent):void
    {
        this.selectedIndex = lastRollOverIndex;
    }

    private function copySelectedToExcel(evt:Event):void
    {
        copyToExcel(selectedItemsInOriginOrder);
    }

    private function copyToExcel(dataList:Object):void
    {
        System.setClipboard(rowsToString(dataList, '	'));
    }

    private function copyTotalToExcel(evt:Event):void
    {
        copyToExcel(dataProvider);
    }

    private function createItem(evt:Event):void
    {
        dispatchEvent(new Event('createItem'));
    }

    private function deleteAll(evt:Event):void
    {
        MessageUtil.actionYes("确认全部删除？", function ():void
        {
            dispatchEvent(new Event('deleteAll'));
        })
    }

    private function deleteItems(evt:Event):void
    {
        MessageUtil.actionYes(rowsToString(multiDelete ? selectedItems : [selectedItem],
                ','), function ():void
        {
            dispatchEvent(new Event('deleteItems'));
        }, '确定删除吗？')
    }

    private function dgItemRollOut(event:ListEvent):void
    {
        for each (var menu:ContextMenuItem in contextMenu.customItems)
            menu.enabled = menuSupport.isAlwaysEnabled(menu);
    }

    private function dgItemRollOver(event:ListEvent):void
    {
        lastRollOverIndex = event.rowIndex;

        for each (var menu:ContextMenuItem in contextMenu.customItems)
            if (!menuSupport.isAlwaysEnabled(menu) && _menuEnableChecker)
                menu.enabled =
                        _menuEnableChecker(menu, this.dataProvider[event.rowIndex]);
            else
                menu.enabled = true;
    }

    private function enableMenu(menuLabel:String, action:Function,
                                separatorBefore:Boolean = false, alwaysEnabled:Boolean = false,
                                withDoubleClick:Boolean = false):void
    {
        menuSupport.createMenuItem(menuLabel, action, separatorBefore, alwaysEnabled);

        if (withDoubleClick && !doubleClickEnabled && !_menuEnableChecker)
        {
            doubleClickEnabled = true;
            addEventListener(ListEvent.ITEM_DOUBLE_CLICK, action);
            replacableDoubleClickHandler = action;
        }
    }

    private function init(event:FlexEvent):void
    {
        resetMenu();
        if (showIndex && columns && columns.length > 0) //自动生成列时，无法添加
        {
            indexColumn = new DataGridColumnPlus;
            indexColumn.headerText = '#'
            indexColumn.setStyle("backgroundColor", 0xeeeeee);
            indexColumn.setStyle("backgroundAlpha", 1);
            indexColumn.setStyle("textAlign", 'center');
            indexColumn.width = 30
            indexColumn.viewable = false;
            indexColumn.editable = false;
            indexColumn.sortable = false
            indexColumn.resizable = false
            indexColumn.labelFunction =
                    function (item:Object, column:DataGridColumn):String
                    {
                        return String(new ArrayCollectionPlus(dataProvider).getItemIndex(item) + 1);
                    };
            var cols:Array = columns;
            cols.unshift(indexColumn);
            columns = cols;
        }
    }

    private function initPop(editable:Boolean = false,
                             multEditable:Boolean = false):PopWindow
    {
        var form:Form = new Form();
        var pop:PopWindow = ContainerUtil.initPopUP('查看', form, -1, -1, 'center');
        for each (var col:DataGridColumn in(editable ? editableColumns : viewableColumns))
        {
            form.addChild(new DataColumnFormItem(this, col, editable, multEditable));
        }

        var controlBar:ControlBar = new ControlBar;
        controlBar.setStyle('horizontalAlign', 'center')
        controlBar.setStyle('paddingTop', 6)
        controlBar.setStyle('paddingBottom', 6)
        if (editable)
        {
            var submit:Function = function (e:Event):void
            {
                if (!ContainerUtil.validate(form))
                {
                    pop.playShake();
                    return;
                }
                pop.showProgress();
                if (!multEditable)
                    dispatchEvent(new SaveItemEvent(showItemProxy));
                else
                    multEditFire();
            }
            if (popEnterSubmit)
                pop.addEventListener('enterKeyDown', submit);
            var reset:Function = function (e:Event):void
            {
                dispatchEvent(new Event('resetEditItem'));
            };
            controlBar.addChild(ComponentUtil.createButton('保存', submit, {name: multEditable ? 'saveMultItem' : 'saveItem'}));
            controlBar.addChild(ComponentUtil.createButton('重置', reset));
        }
        else
        {
            controlBar.addChild(ComponentUtil.createButton('上一个', function (e:Event):void
            {
                selectedIndex =
                        (selectedIndex - 1 + dataProvider.length) % dataProvider.length;
                showItem = selectedItem;
            }));
            controlBar.addChild(ComponentUtil.createButton('下一个', function (e:Event):void
            {
                selectedIndex = (selectedIndex + 1) % dataProvider.length;
                showItem = selectedItem;
            }));
        }
        pop.addChild(controlBar);
        return pop;
    }

    private function modifyItem(evt:Event):void
    {
        dispatchEvent(new Event('modifyItem'));
    }

    private function multCreate(evt:ContextMenuEvent):void
    {
        var ht:String = '';
        editableColumns.forEach(function (col:*, index:int, array:Array):void
        {
            ht = ht.concat(col.headerText);
            if (index < array.length - 1)
                ht = ht.concat(',');
        })
        multCreateInput.headerTexts = ht;
        PopUpManager.addPopUp(multCreateInput, this, true)
    }

    private function multCreateConfirm(e:Event):void
    {
        var newList:Array = [];
        var eCols:Array = editableColumns;
        for each (var row:String in multCreateInput.tta.text.split(/[\r\n]/))
        {
            row = StringUtil.trim(row);
            if (row.length > 0)
            {
                var cols:Array = row.split(/[\t|\,|，]/);
                var newItem:Object = {};
                for (var i:int = 0; i <= cols.length; i++)
                {
                    if (i >= eCols.length)
                        break;
                    var colp:DataGridColumnPlus = eCols[i];
                    if (colp.dataField)
                    {
                        if (colp.asControl == 'ComboBox')
                        {
                            var inBox:Object =
                                    new ArrayCollectionPlus(colp.controlProps.dataProvider).findByField(colp.controlProps.labelField,
                                            cols[i])
                            if (inBox == null && colp.controlProps.valueField)
                            {
                                MessageUtil.showMessage(colp.headerText.concat('中不存在[',
                                        cols[i], ']，请检查！'));
                                return;
                            }
                            newItem[colp.dataField.split('.')[0]] = inBox;
                        }
                        else if (colp.asControl == 'CheckBox')
                            ObjectUtils.setValue(newItem, colp.dataField,
                                    (cols[i] != 'false' && cols[i] != '0'));
                        else
                        {
                            var sv:String = StringUtil.trim(cols[i]);
                            if (colp.asNumber)
                            {
                                var nv:Number = StringUtil.parseNumber(sv);
                                ObjectUtils.setValue(newItem, colp.dataField,
                                        (isNaN(nv) ? null : nv))
                            }
                            else
                                ObjectUtils.setValue(newItem, colp.dataField, sv)
                        }
                    }
                }
                newList.push(newItem);
            }
        }
        if (newList.length > 0)
            dispatchEvent(new MultCreateEvent(newList));
        PopUpManager.removePopUp(multCreateInput);
    }

    private function multEditFire():void
    {
        var newItem:Object = {};
        for each (var colp:DataGridColumnPlus in editableColumns)
        {
            if (colp.dataField && colp.multEditable &&
                    ObjectUtils.getValue(showItemProxy,
                            Constants.MULT_EDIT_FLAG + colp.dataField))
            {
                if (colp.asControl == 'ComboBox' && !colp.controlProps.dataField)
                {
                    var f:String = colp.dataField.split('.')[0];
                    newItem[f] = showItemProxy[f];
                }
                else
                    ObjectUtils.setValue(newItem, colp.dataField,
                            ObjectUtils.getValue(showItemProxy, colp.dataField));
            }
        }

        var sis:Array = [];
        for each (var it:Object in selectedItems)
            sis.push(ObjectUtils.mergeObject(it, newItem));

        dispatchEvent(new SaveMultItemEvent(sis));
    }

    private function onHeaderRelease(event:DataGridEvent):void
    {
        if (!globalSort)
            return;
        event.preventDefault();
        var col:DataGridColumn = columns[event.columnIndex];

        if (col.sortable && col.dataField)
        {
            addOrder(col.dataField);
            dispatchEvent(new Event('changeOrder'));
        }
    }

    private function refreshHeadText():void
    {
        for each (var col:DataGridColumn in columns)
        {
            col.headerText = getCleanHeader(col);

            if (col.sortable && col.dataField)
                for (var i:int = 0; i < orderList.length; i++)
                    if (orderList[i].sortField == col.dataField)
                    {
                        col.headerText =
                                col.headerText.concat((orderList[i].order == 'asc' ? '↑' : '↓'),
                                        multiSort ? i + 1 : '');
                    }
        }
    }

    private function saveToExcel(evt:Event):void
    {
        saveAsExcel(dataProvider, exportName ? exportName : UIDUtil.createUID());
    }
}
}

