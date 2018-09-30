package ns.flex.util
{

public class ConstraintCommon
{
    public static const ID_CARD_EXP:String =
            '^[1-9][0-7]\\d{4}(18|19|20)?\\d{2}(0[1-9]|1[012]])[0-3]\\d\\d{3}(\\d|X|x)?$';
    public static const ID_CARD_NO:Object =
            {
                noSpace: true, expression: ID_CARD_EXP, required: true, imeDisabled: true,
                restrict: '0-9X', maxChars: 18
            };

    public static const account:Object =
            {
                expression: "\\w{2}", required: true, noSpace: true, imeDisabled: true,
                restrict: '0-9A-Za-z_'
            }
    public static const digitalString:Object =
            {
                required: true, noSpace: true, imeDisabled: true, restrict: '0-9',
                errorTip: '请输入数字'
            }
    public static const english:Object =
            {
                imeDisabled: true, required: true, expression: '^[\\x00-\\xff]*$',
                errorTip: '请输入英文'
            };
    public static const integer:Object =
            {
                noSpace: true, expression: '^-{0,1}\\d*$', required: true, imeDisabled: true,
                restrict: '\\-0-9', maxChars: 9
            };
    public static const integerPositive:Object =
            {
                noSpace: true, expression: '^\\d*$', required: true, imeDisabled: true,
                restrict: '0-9', maxChars: 9
            };
    public static const justRequired:Object = {required: true};
    public static const requiredNoSpace:Object = {required: true, noSpace: true};
    public static const number:Object =
            {
                noSpace: true, expression: '^-{0,1}\\d+\\.{0,1}\\d*$', required: true,
                imeDisabled: true, restrict: '\\-\\.0-9', maxChars: 18
            };
    public static const numberPositive:Object =
            {
                noSpace: true, expression: '^\\d+\\.{0,1}\\d*$', required: true,
                imeDisabled: true, restrict: '\\.0-9', maxChars: 18
            };
    public static const password:Object =
            {
                expression: "\\w{2}", required: true, noSpace: true, imeDisabled: true,
                displayAsPassword: true, restrict: '\u0021-\u007E'
            };
    public static const phone:Object =
            {
                noSpace: true, expression: '^[\\-\\+\\.\\(\\)\\d]{5,32}$', required: true,
                imeDisabled: true, restrict: '\\-+\\.()0-9'
            };
    public static const url:Object =
            {
                noSpace: true, expression: '^[a-zA-Z]+://.*$', maxChars: 256, autoTrim: true,
                errorTip: '请输入正确的网址'
            };
    public static const time:Object =
            {
                noSpace: true, expression: '^[0-2]{0,1}[0-9]:[0-5]{0,1}[0-9]$', required: true, autoTrim: true,
                errorTip: '请输入正确的24小时格式时间'
            };

    /**
     * 限制输入字符最少数量
     * @param len 最少数
     * @param required
     * @param noSpace
     * @param imeDisabled
     * @return
     */
    public static function forLength(len:int, required:Boolean = true,
                                     noSpace:Boolean = true, imeDisabled:Boolean = true):Object
    {
        return {
            expression: String(".{").concat(len, "}"), required: required,
            noSpace: noSpace, imeDisabled: imeDisabled
        };
    }
}
}

