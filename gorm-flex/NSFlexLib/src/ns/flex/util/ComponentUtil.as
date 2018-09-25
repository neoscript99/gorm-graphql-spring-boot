/**
 * Created by Neo on 2017-09-19.
 */
package ns.flex.util
{
import flash.events.MouseEvent;

import mx.controls.Button;

public class ComponentUtil
{
    static public function createButton(label:String, clickHandler:Function, props:Object = null):Button
    {
        var button:Button = new Button();
        button.label = label;
        ObjectUtils.copyProperties(button, props);
        button.addEventListener(MouseEvent.CLICK, clickHandler);
        return button;
    }
}
}
