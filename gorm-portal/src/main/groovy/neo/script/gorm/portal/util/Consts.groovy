package neo.script.gorm.portal.util

import static neo.script.util.JsonUtil.*

class Consts {
    static STYLE_FLEX_ROW = toJson(["display": "flex", "justifyContent": "center", "flexWrap": "wrap"])
    static STYLE_FLEX_COL = toJson(["display": "flex", "flexDirection": "column"])
    static DEFAULT_COL_PROPS = toJson(["offset": 0])
}
