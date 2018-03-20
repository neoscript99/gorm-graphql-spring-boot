package neo.script.gorm.general.config

import org.springframework.boot.env.PropertySourceLoader
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.util.ClassUtils

/**
 * 加载application.groovy配置文件
 *
 * <a href="https://blog.jdriven.com/2016/07/spring-sweets-using-groovy-configuration-propertysource/">原始文章</a>
 */
public class GroovyConfigPropertySourceLoader implements PropertySourceLoader {

    /**
     * Allowed Groovy file extensions for configuration files.
     *
     * @return List of extensions for Groovy configuration files.
     */
    @Override
    public String[] getFileExtensions() {
        return ["groovy"] as String[];
    }

    /**
     * Load Groovy configuration file with {@link groovy.util.ConfigSlurper}.
     */
    @Override
    public PropertySource<?> load(
            final String name,
            final Resource resource,
            final String profile) throws IOException {

        if (isConfigSlurperAvailable()) {
            final ConfigSlurper configSlurper = profile != null ?
                    new ConfigSlurper(profile) :
                    new ConfigSlurper();

            // Add some extra information that is accessible
            // in the Groovy configuration file.
            configSlurper.setBinding(createBinding(profile));
            final ConfigObject config = configSlurper.parse(resource.getURL());

            // Return ConfigObjectPropertySource if configuration
            // has key/value pairs.
            return config.isEmpty() ?
                    null :
                    new GroovyConfigPropertySource(name, config);
        }

        return null;
    }

    private boolean isConfigSlurperAvailable() {
        return ClassUtils.isPresent("groovy.util.ConfigSlurper", null);
    }

    private Map<String, Object> createBinding(final String profile) {
        final Map<String, Object> bindings = new HashMap<>();
        bindings.put("userHome", System.getProperty("user.home"));
        bindings.put("appDir", System.getProperty("user.dir"));
        bindings.put("springProfile", profile);
        return bindings;
    }

}