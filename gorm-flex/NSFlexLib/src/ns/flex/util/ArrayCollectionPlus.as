package ns.flex.util {
import mx.collections.ArrayCollection;
import mx.collections.ArrayList;

import ns.flex.common.Messages;

/**
 * 集合类
 * @author wangchu
 */
public class ArrayCollectionPlus extends ArrayCollection {
    public static const EMPTY:ArrayCollectionPlus = new ArrayCollectionPlus();

    public function ArrayCollectionPlus(source:Object = null) {
        super(ArrayUtil.toArray(source));
    }

    public static function withAll(source:Object,
                                   labelField:String):ArrayCollectionPlus {
        var all:Object = {}
        all[labelField] = Messages.ALL;
        return withFirst(source, all);
    }

    public static function withFirst(source:Object, first:Object):ArrayCollectionPlus {
        return new ArrayCollectionPlus(source).addFirst(first);
    }

    /**
     * 增加到最前面
     * @param item 增加的项
     * @return 集合本身
     */
    public function addFirst(item:Object):ArrayCollectionPlus {
        addItemAt(item, 0);
        return this;
    }

    public function each(f:Function):void {
        for (var i:int = 0; i < this.length; i++)
            f(getItemAt(i))
    }

    /**
     * 查询项目
     * @param f 函数f(item)，返回true代表符合查询条件
     * @return 查找到的项或null
     */
    public function find(f:Function):Object {
        for (var i:int = 0; i < this.length; i++) {
            var item:Object = this.getItemAt(i);

            if (f(item))
                return item;
        }
        return null;
    }

    public function findAllByField(field:String, value:*):ArrayCollectionPlus {
        var acp:ArrayCollectionPlus = new ArrayCollectionPlus();
        for each (var item:* in this)
            if (ObjectUtils.getValue(item, field) == value)
                acp.addItem(item);
        return acp;
    }


    public function countByField(field:String, value:*):int {
        var num:int = 0;
        for each (var item:* in this)
            if (ObjectUtils.getValue(item, field) == value)
                num++;
        return num;
    }

    [Bindable("collectionChange")]
    /**
     * 根据字段查找集合项
     * @param field 字段名称
     * @param value 值
     * @return  找到的集合项或null
     */
    public function findByField(field:String, value:*):Object {
        for each (var item:* in this)
            if (ObjectUtils.getValue(item, field) == value)
                return item;
        return null;
    }

    public function getFieldArray(field:String):Array {
        var fieldArray:Array = [];
        for (var i:int = 0; i < length; i++) {
            fieldArray.push(ObjectUtils.getValue(getItemAt(i), field));
        }
        return fieldArray;
    }

    /**
     * 查找符合条件的所有集合项的集合
     * @param f 函数，f(item)为true代表符合条件
     * @return  新集合
     */
    public function grep(f:Function):ArrayCollectionPlus {
        var acp:ArrayCollectionPlus = new ArrayCollectionPlus();

        for (var i:int = 0; i < this.length; i++) {
            var item:Object = this.getItemAt(i);

            if (f(item))
                acp.addItem(item);
        }
        return acp;
    }

    /**
     * 删除符合条件的所有集合项
     * @param f 函数f(item)为true时代表符合条件
     */
    public function remove(f:Function):ArrayCollectionPlus {
        for (var i:int = this.length - 1; i >= 0; i--) {
            var item:Object = this.getItemAt(i);

            if (f(item))
                this.removeItemAt(i);
        }
        return this;
    }

    /**
     * 删除某一对象
     */
    public function removeItem(item:Object):ArrayCollectionPlus {
        var index:int = getItemIndex(item);
        if (index > -1)
            this.removeItemAt(index);
        return this;
    }

    /**
     * 相加每个集合项对应的计算值
     * @param f 函数f(item)为计算结果
     * @return 总和
     */
    public function sum(f:Function):Object {
        var value:Object;

        if (this.length > 0)
            value = f(this.getItemAt(0));

        for (var i:int = 1; i < this.length; i++) {
            value += f(this.getItemAt(i));
        }
        return value;
    }

    /**
     * 将集合转化为二维数组，如：[{a:1,b:2},{a:3,b:4}]-->[[1,2],[3,4]]
     * @param fields 包含字段，如为空，包含所有字段
     * @return
     */
    public function toBiArray(...fields):Array {
        var array:Array = [];
        toArray().forEach(function (item:*, index:int, ar:Array):void {
            var innerArray:Array = [];

            if (fields.length > 0)
                for each (var field:String in fields)
                    innerArray.push(item[field]);
            else
                for each (var prop:* in item)
                    innerArray.push(prop);
            array.push(innerArray);
        });
        return array;
    }

    public function addItems(...items):void {
        addAll(new ArrayList(items))
    }

    public function addItemArray(items:Array):void {
        if (items)
            addAll(new ArrayList(items))
    }
}
}

