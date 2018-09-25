package ns.flex.controls
{
import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.FormItem;
import mx.containers.HBox;
import mx.controls.CheckBox;
import mx.controls.LinkButton;
import mx.controls.dataGridClasses.DataGridColumn;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.Operation;

import ns.flex.common.Constants;
import ns.flex.file.Downloader;
import ns.flex.file.Uploader;
import ns.flex.util.DateUtil;
import ns.flex.util.ObjectUtils;
import ns.flex.util.StringUtil;

public class DataColumnFormItem extends FormItem
{
    private var _component:UIComponent;

    public function DataColumnFormItem(dgp:DataGridPlus, col:DataGridColumn,
                                       editable:Boolean, multEditable:Boolean)
    {
        super();
        percentWidth = 100;
        editable = (editable && col.dataField);

        if (col is DataGridColumnPlus)
        {
            var colp:DataGridColumnPlus = DataGridColumnPlus(col)
            multEditable = (editable && multEditable && colp.multEditable);

            if ('CheckBox' == colp.asControl && col.dataField)
                _component = asCheckBox(dgp, col, editable);
            else if ('ComboBox' == colp.asControl && editable)
                _component = asComboBox(dgp, colp);
            else if ('AutoComplete' == colp.asControl)
                _component = asAutoComplete(dgp, colp, editable);
            else if ('LinkButton' == colp.asControl && !editable)
                _component = asLinkButton(dgp, colp);
            else if ('Uploader' == colp.asControl)
                _component = asUploader(dgp, colp, editable);
            else if (('DateField' == colp.asControl || 'DateString' == colp.asControl) &&
                    editable)
                _component = asDateField(dgp, colp);
            else
                _component = asText(dgp, col, editable, 'TextArea' == colp.asControl);
        }
        else
        {
            multEditable = false;
            _component = asText(dgp, col, editable);
        }

        label = StringUtil.toLine(DataGridPlus.getCleanHeader(col));
        _component.name = label;
        this.name = label+'.FormItem';
        //批量修改，后面加个选择框
        if (multEditable)
        {
            var mhbox:HBox = new HBox;
            var mcb:CheckBox = new CheckBox();
            BindingUtils.bindSetter(function (value:Boolean):void
            {
                //加后缀进行区别，防止冲突
                ObjectUtils.setValue(dgp.showItemProxy,
                        Constants.MULT_EDIT_FLAG + col.dataField, value);
                _component.enabled = value;
            }, mcb, 'selected');

            BindingUtils.bindSetter(function (value:Object):void
            {
                mcb.selected = false;
            }, dgp, 'showItemProxy');
            mhbox.addChild(_component);
            mhbox.addChild(mcb);
            addChild(mhbox);
        }
        else
            addChild(_component);
    }

    public function get component():UIComponent
    {
        return _component;
    }

    private function asAutoComplete(dgp:DataGridPlus, colp:DataGridColumnPlus,
                                    editable:Boolean):UIComponent
    {
        var ac:AutoCompletePlus = new AutoCompletePlus;
        ObjectUtils.copyProperties(ac, colp.controlProps);
        ac.editable = editable;
        ac.constraints = colp.constraints;
        var getSelected:Operation = colp.controlProps.getSelected;
        //原选择项可以来自两个地方：1、远程查询；2、当前对象数据。 1优先
        //1、远程查询需指定getSelected，该属性为远程方法
        if (getSelected)
        {
            getSelected.addEventListener(ResultEvent.RESULT, function (e:ResultEvent):void
            {
                ac.selectedItems = e.result as ArrayCollection;
                //只读时dataProvider不用在mxml中设置，直接等于selectedItems
                if (!ac.editable)
                    ac.dataProvider = ac.selectedItems;
            })

            if (!colp.controlProps.getSelectedSend)
            {
                //如果不处理，因为有查看编辑两个对话框，这个方法会调用两次
                colp.controlProps.getSelectedSend = true;
                BindingUtils.bindSetter(function (value:Object):void
                {
                    getSelected.send(value)
                }, dgp, 'showItemProxy');
            }
        }
        else if (ac.labelField)//2、当前对象数据通过列的dataField获取
        {
            BindingUtils.bindSetter(function (value:Object):void
            {
                //一般colp.dataField为type.name, ac.labelField为name，但这里取showItemProxy.type
                ac.selectedItem = ObjectUtils.getValue(value, colp.dataField.replace('.' + ac.labelField, ''))
            }, dgp, 'showItemProxy');
        }
        if (editable)
            BindingUtils.bindSetter(function (value:Object):void
            {
                //allowNewValues为true时取label就够了，因为新输入的只能是label
                //allowMultipleSelection为false时直接取selectedItem
                var v:Object;
                if (ac.allowNewValues)
                    v = ac.selectedLabels
                else if (ac.selectedItems)
                    v = ac.allowMultipleSelection ? ac.selectedItems : ac.selectedItem;
                ObjectUtils.setValue(dgp.showItemProxy, colp.dataField.replace('.' + ac.labelField, ''), v);
            }, ac, 'selectedLabels');

        return ac;
    }

    private function asCheckBox(dgp:DataGridPlus, col:DataGridColumn,
                                editable:Boolean):UIComponent
    {
        var cb:CheckBox = new CheckBox();
        cb.enabled = editable;
        BindingUtils.bindSetter(function (value:Object):void
        {
            cb.selected = value[col.dataField];
            value[col.dataField] = cb.selected;
        }, dgp, 'showItemProxy');

        if (editable)
            BindingUtils.bindSetter(function (value:Boolean):void
            {
                ObjectUtils.setValue(dgp.showItemProxy, col.dataField, value);
            }, cb, 'selected');
        return cb;
    }

    private function asComboBox(dgp:DataGridPlus, colp:DataGridColumnPlus):UIComponent
    {
        var cbp:ComboBoxPlus = new ComboBoxPlus();
        ObjectUtils.copyProperties(cbp, colp.controlProps);
        BindingUtils.bindSetter(function (value:Object):void
        {
            var defaultStr:String = StringUtil.trim(colp.itemToLabel(value))
            if (defaultStr)
                cbp.defaultLabel = defaultStr;
            else
                cbp.selectedIndex = 0;
        }, dgp, 'showItemProxy');
        BindingUtils.bindSetter(function (value:Object):void
        {
            if (value)
            {
                //write col.dataField if dataField is set
                if (colp.controlProps.dataField)
                    dgp.showItemProxy[colp.dataField] =
                            value[colp.controlProps.dataField];
                else //set nest first field
                    dgp.showItemProxy[colp.dataField.split('.')[0]] = value;
            }
        }, cbp, 'selectedItem');
        return cbp;
    }

    private function asDateField(dgp:DataGridPlus,
                                 colp:DataGridColumnPlus):UIComponent
    {
        var dfp:DateFieldPlus = new DateFieldPlus();
        dfp.constraints = colp.constraints;

        if ('DateString' == colp.asControl)
        {
            BindingUtils.bindSetter(function (value:Object):void
            {
                trace('asDateField-', 'set dfp.selectedDate');
                if (ObjectUtils.getValue(value, colp.dataField))
                    dfp.selectedDate =
                            DateUtil.stringToDate(String(ObjectUtils.getValue(value,
                                    colp.dataField)), dfp.formatString);
                else
                { //不能设text为null，否则flex出错
                    dfp.resetDefault();
                    ObjectUtils.setValue(dgp.showItemProxy, colp.dataField, dfp.text);
                }
            }, dgp, 'showItemProxy');
            BindingUtils.bindSetter(function (value:Object):void
            {
                ObjectUtils.setValue(dgp.showItemProxy, colp.dataField, value);
            }, dfp, 'text');
        }
        else
        {
            BindingUtils.bindSetter(function (value:Object):void
            {
                if (ObjectUtils.getValue(value, colp.dataField))
                    dfp.selectedDate =
                            ObjectUtils.getValue(value, colp.dataField) as Date;
                else
                {
                    dfp.resetDefault();
                    ObjectUtils.setValue(dgp.showItemProxy, colp.dataField,
                            dfp.selectedDate);
                }
            }, dgp, 'showItemProxy');
            BindingUtils.bindSetter(function (value:Object):void
            {
                ObjectUtils.setValue(dgp.showItemProxy, colp.dataField, value);
            }, dfp, 'selectedDate');
        }
        return dfp;
    }

    private function asLinkButton(dgp:DataGridPlus, col:DataGridColumn):UIComponent
    {
        var lb:LinkButton = new LinkButton();
        lb.maxWidth = 480;
        BindingUtils.bindSetter(function (value:Object):void
        {
            lb.label = value[col.dataField];
        }, dgp, 'showItemProxy');
        lb.addEventListener(MouseEvent.CLICK, function (e:MouseEvent):void
        {
            navigateToURL(new URLRequest(lb.label), '_blank')
        });
        return lb;
    }

    private function asText(dgp:DataGridPlus, col:DataGridColumn, editable:Boolean,
                            asTextArea:Boolean = false):UIComponent
    {
        var textInput:UIComponent =
                (col.wordWrap || asTextArea) ? new TextAreaPlus() : new TextInputPlus();

        if (col is DataGridColumnPlus)
            textInput['constraints'] = DataGridColumnPlus(col).constraints;

        textInput.setStyle('textAlign', col.getStyle('textAlign'));
        textInput['editable'] = editable;
        BindingUtils.bindSetter(function (value:Object):void
        {
            if (col['asNumber'] && col['isSeparateThousands'])
                textInput['text'] = col.itemToLabel(value).replace(',', '');
            else
                textInput['text'] = col.itemToLabel(value);
        }, dgp, 'showItemProxy');

        if (editable)
            BindingUtils.bindSetter(function (value:String):void
            {
                var nv:Number = StringUtil.parseNumber(value);
                //NaN传到后台无法处理，改为传null
                ObjectUtils.setValue(dgp.showItemProxy, col.dataField,
                        col['asNumber'] ? (isNaN(nv) ? null : nv) : value);
                var head:String = StringUtil.toLine(DataGridPlus.getCleanHeader(col));
                label =
                        textInput['maxChars'] ? head.concat('(', textInput['remainSize'],
                                ')') : head;
            }, textInput, 'text');
        return textInput;
    }

    private function asUploader(dgp:DataGridPlus, colp:DataGridColumnPlus,
                                editable:Boolean):UIComponent
    {
        var ud:UIComponent;
        if (editable)
        {
            var up:Uploader = new Uploader;
            ud = up;
            up.addEventListener('change', function (e:Event):void
            {
                ObjectUtils.setValue(dgp.showItemProxy, colp.dataField, up.info);
            });
            ObjectUtils.copyProperties(ud, colp.controlProps);
        }
        else
            ud = new Downloader(colp.controlProps.destination);

        BindingUtils.bindSetter(function (value:Object):void
        {
            var id:Object = ObjectUtils.getValue(value, colp.controlProps.ownerIdField);
            ud['ownerId'] = id ? id : null;
        }, dgp, 'showItemProxy');
        return ud;
    }
}
}

