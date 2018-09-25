package ns.gflex.util;

import org.springframework.core.convert.converter.Converter;

/**
 * Created by Neo on 2017-07-05.
 */
public class Int2LongConverter implements Converter<Integer, Long> {

    @Override
    public Long convert(Integer source) {
        return source.longValue();
    }
}