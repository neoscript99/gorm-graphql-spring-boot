package neo.script.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.transform.CompileStatic;
import groovy.util.logging.Slf4j;


@Slf4j
@CompileStatic
public class JsonUtil {
    private static ObjectMapper objectMapper = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    /**
     * 对象转为json字符串
     *
     * @param o
     * @return
     * @author 王楚
     * @version 2016年6月28日
     */
    public static String toJson(Object o) {
        return toJson(o, true);
    }

    public static String toJson(Object o, boolean isPretty) {
        try {
            String str = isPretty ? objectMapper
                    .writerWithDefaultPrettyPrinter().writeValueAsString(o)
                    : objectMapper.writeValueAsString(o);
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String content, Class<T> type, boolean ignoreError = true) {
        try {
            return objectMapper.readValue(content, type);
        } catch (IOException e) {
            e.printStackTrace();
            if (ignoreError)
                return null;
            else
                throw e;
        }
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> type) {
        return fromJson(toJson(map, false), type);
    }

    public static Map<String, Object> beanToMap(Object bean) {
        return fromJson(toJson(bean, false), Map.class);
    }

    public static <T> List<T> mapListToBeanList(
            List<Map<String, Object>> mapList, Class<T> type) {
        return json2Collection(toJson(mapList, false), List.class, type);
    }

    public static <T> List<T> beanListToMapList(List beanList) {
        return json2Collection(toJson(beanList, false), List.class, Map.class);
    }

    public static <T> T json2Collection(String content,
                                        Class<?> collectionClass, Class<?>... elementClasses) {
        try {
            return objectMapper.readValue(content,
                    getCollectionType(collectionClass, elementClasses));
        } catch (IOException e) {
            log.error('json转为集合失败', e)
            return null;
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass,
                                             Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(
                collectionClass, elementClasses);
    }

    /**
     * json转换为单层Map，嵌套属性保持json格式
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToOneLevelMap(String json) {
        return fromJson(json, Map).collectEntries([:]) { k, v ->
            [k, (v instanceof Map) ? toJson(v, false) : v]
        }
    }
}
