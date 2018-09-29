package modules {
import flash.display.DisplayObject;
import flash.events.Event;

import mx.containers.ViewStack;
import mx.core.Container;

import ns.flex.module.AbstractModule;

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
        percentWidth = percentHeight = 100;
        addEventListener(Event.CHANGE, onChange)
        addChild(new Welcome());
        addChild(new About());
        addChild(comingModule);
    }

    public function addModules(moreModules:Array):void {
        moduleList.addItemArray(moreModules);
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

    private function onChange(e:Event):void {
        if (this.selectedChild is AbstractModule)
            (this.selectedChild as AbstractModule).beforeDisplay();
    }
}
}
