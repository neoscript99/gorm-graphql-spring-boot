package neo.script.gorm.graphql.binding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.grails.gorm.graphql.binding.GraphQLDataBinder
import org.springframework.beans.BeanUtils

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
        BeanUtils.copyProperties(objectMapper.readValue(jsonData, object.class), object)
    }
}