package neo.script.gorm.general.config

import org.springframework.core.env.MapPropertySource

/**
 * Created by Neo on 2017-10-25.
 * Property source that supports {@link ConfigObject}. The {@link ConfigObject}
 * is flattened and all functionality is delegated to the
 * {@link MapPropertySource}.
 */
class GroovyConfigPropertySource extends MapPropertySource {

    public GroovyConfigPropertySource(final String name, final ConfigObject config) {
        super(name, config.flatten());
    }

}