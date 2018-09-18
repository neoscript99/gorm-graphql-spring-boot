package neo.script.gorm.graphql.binding

import neo.script.util.JsonUtil
import org.grails.gorm.graphql.binding.GraphQLDataBinder

class GormGraphQLDataBinder implements GraphQLDataBinder {

    @Override
    void bind(Object object, Map data) {
        def newObject = JsonUtil.mapToBean(data, object.class)
        //1、遍历data，对其中属性进行赋值
        //2、如果属性为GormEntity，通过id查询实体
        //3、遍历赋值，如果直接复制整个newObject，会带入null值，因为data不一定是全量属性
        //4、只需处理第一层赋值，嵌套属性已通过第2步查询获得
        data.each { k, v ->
            /*application.groovy中设置'*'(cascade:"none")后，不再需要第2步，通过id查询实体
            Object value = newObject."$k";
            object."$k" = (value != null && GormEntity.isAssignableFrom(value.class)) ?
                    value.class.get((value as GormEntity).ident()) : value
            */
            object."$k" = newObject."$k"
        }
    }
}