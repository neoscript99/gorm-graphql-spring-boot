package modules {
import flash.display.DisplayObject;
import flash.display.DisplayObject;

import mx.containers.ViewStack;
import mx.core.Container;

import ns.flex.util.ArrayCollectionPlus;

public class Views extends ViewStack {


    private const moduleList:ArrayCollectionPlus =
            new ArrayCollectionPlus([
                new Profile(), new Note(),
                new Param(), new User(),
                new Role()
            ]);
    private const comingModule:ComingModule = new ComingModule();

    public function Views() {
        addChild(new Welcome());
        addChild(new About());
        addChild(comingModule);
    }

    public function displayModule(name:String):void {
        var child:DisplayObject = getChildByName(name);
        if (child)
            selectedChild = child as Container;
        else {
            var module:DisplayObject = moduleList.find(function (item:DisplayObject):Boolean {
                return item.name == name
            }) as DisplayObject;
            if (module) {
                addChild(module);
                selectedChild = module as Container;
            }
            else
                selectedChild = comingModule;
        }
    }

    public function addModule(module:DisplayObject):void {
        moduleList.addItem(module)
    }

    public function addModules(modules:Array):void {
        moduleList.addItemArray(modules);
    }
}
}
