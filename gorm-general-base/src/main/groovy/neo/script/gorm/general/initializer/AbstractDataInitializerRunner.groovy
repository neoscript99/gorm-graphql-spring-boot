package neo.script.gorm.general.initializer

import org.springframework.transaction.annotation.Transactional
import groovy.util.logging.Slf4j
import neo.script.gorm.general.repositories.GeneralRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.core.type.ClassMetadata
import org.springframework.core.type.classreading.CachingMetadataReaderFactory
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.util.ClassUtils
import org.springframework.util.SystemPropertyUtils

/**
 * DataInitializer方式的数据初始化
 * <p> 本runner需依赖 DomainInitRunner
 * <p> 定义继承本类的 CommandLineRunner
 * <p> 优先级低于 @Order(InitializeOrder.DOMAIN_INIT)
 * <p> 通过命令行参数"--init"发起本初始化过程
 * Created by Neo on 2017-09-28.
 */
@Slf4j
abstract class AbstractDataInitializerRunner implements CommandLineRunner {
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    ApplicationArguments applicationArguments

    String getBasePackage() {
        def pkg = this.class.getPackage()
        log.debug("DataInitializer BasePackage: {}", pkg)
        return pkg.name
    }

    @Override
    @Transactional
    void run(String... args) throws Exception {
        if (applicationArguments.containsOption('init') || System.getProperty('init') != null) {
            log.debug("$basePackage 中DataInitializer方式数据初始化开始")
            doInit(generalRepository, applicationContext, basePackage)
            log.debug("$basePackage 中DataInitializer方式数据初始化完成")
        }
    }

    /**
     * 使用spring工具类扫描包，运行实现了DataInitializer接口的init方法，进行数据初始化
     * Created by Neo on 2017-08-22.
     * @param generalRepository
     * @param applicationContext
     * @param basePackage
     * @param pattern
     */
    void doInit(GeneralRepository generalRepository, ApplicationContext applicationContext, String basePackage, String pattern = "**/*.class") {

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + pattern;
        try {
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            List<Class<DataInitializer>> initClasses = []
            for (Resource resource : resources) {
                ClassMetadata metadata = metadataReaderFactory.getMetadataReader(resource).getClassMetadata()
                if (metadata && metadata.isConcrete()
                        && metadata.getInterfaceNames().contains(DataInitializer.name)) {
                    String cls = metadata.getClassName()
                    initClasses << Class.forName(cls)
                }
            }
            initClasses.sort(AnnotationAwareOrderComparator.INSTANCE)
            initClasses.each {
                log.info("data initialize by {}", it)
                DataInitializer dataInitializer = it.newInstance()
                dataInitializer.generalRepository = generalRepository
                dataInitializer.applicationContext = applicationContext
                dataInitializer.init()
            }
        } catch (Exception e) {
            log.error("doInit失败", e);
        }
    }

}
