package neo.script.gorm.graphql.binding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.grails.gorm.graphql.binding.GraphQLDataBinder

class GormGraphQLDataBinder implements GraphQLDataBinder {

    private static ObjectMapper objectMapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Override
    void bind(Object object, Map data) {
        String jsonData = objectMapper.writeValueAsString(data);
        def newObject = objectMapper.readValue(jsonData, object.class)
        //遍历data，对其中属性进行赋值
        //如果直接复制newObject，会带入null值，因为data不一定是全量属性
        //暂时只处理第一层赋值，如果需要处理嵌套属性，需再优化
        data.each { k, v ->
            object."$k" = newObject."$k"
        }
    }
}