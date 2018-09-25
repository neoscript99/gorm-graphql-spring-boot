package ns.flex.controls
{
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.ui.Keyboard;

import mx.controls.TextArea;
import mx.events.FlexEvent;

import ns.flex.util.ObjectUtils;
import ns.flex.util.RegExpValidatorPlus;
import ns.flex.util.StringUtil;
import ns.flex.util.UIUtil;
import ns.flex.util.Validatable;
import ns.flex.util.ValidatorUtil;

public class TextAreaPlus extends TextArea implements Validatable
{
    [Inspectable(category="General")]
    public var autoTrim:Boolean = true;
    private const THRESHOLD_SIZE:int = 64;
    [Inspectable(category="General")]
    private var validator:RegExpValidatorPlus;

    public function TextAreaPlus()
    {
        super();
        addEventListener(FlexEvent.VALUE_COMMIT, onValueCommit);
        addEventListener(KeyboardEvent.KEY_DOWN, onKeyDown);
        addEventListener(MouseEvent.ROLL_OVER, UIUtil.showSizeTip);
        addEventListener(MouseEvent.ROLL_OUT, UIUtil.destroyTip);
        maxChars = 64;
        height = 60;
    }

    public function set constraints(value:Object):void
    {
        if (value)
        {
            if (!validator)
                validator = new RegExpValidatorPlus(this);
            ObjectUtils.copyProperties(this, value);

            if (maxChars > THRESHOLD_SIZE)
            {
                if (!value.width)
                    width = Math.min(maxChars / THRESHOLD_SIZE, 3) * 160;
                if (!value.height)
                    height = Math.min(maxChars / THRESHOLD_SIZE, 3) * 60;
            }
            validator.copyProperties(value);
        }
    }

    [Bindable("textChanged")]
    [Bindable("maxCharsChanged")]
    public function get remainSize():int
    {
        return maxChars - text.length;
    }

    [Bindable("valueCommit")]
    public function get validated():Boolean
    {
        return ValidatorUtil.validate(validator);
    }

    public function set zoom(times:Number):void
    {
        width = times * 160;
        height = times * 60;
    }

    private function onKeyDown(evt:KeyboardEvent):void
    {
        trace(this.className, evt.target, evt)
        if (evt.keyCode == Keyboard.ENTER)
            evt.stopImmediatePropagation();
    }

    private function onValueCommit(e:Event):void
    {
        if (autoTrim)
            text = StringUtil.trim(text);
    }
}
}

