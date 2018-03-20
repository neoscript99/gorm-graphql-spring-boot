package neo.script.gorm.data.initializer

import com.google.common.collect.Sets
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import neo.script.gorm.data.initializer.initialize.InitializeDomian
import neo.script.gorm.data.initializer.initialize.InitializeOrder
import neo.script.gorm.general.repositories.GeneralRepository
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

import java.lang.reflect.Field
/**
 * InitializeDomian方式数据初始化
 * <p> InitializeDomian方式为最高优先级，所有DataInitializer方式的数据初始化优先级必须低于本类
 * <p> 通过命令行参数"--init"执行初始化，同时如果传入profiles执行对应的多套初始化方案，
 * <p> 命令行如果未传profiles，默认为[default],也就是注解默认值
 * Created by Neo on 2017-09-27.
 * @see InitializeDomian
 */
@Component
@Order(InitializeOrder.DOMAIN_INIT)
@Slf4j
class DomainInitRunner implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    Environment environment
    @Autowired
    ApplicationArguments applicationArguments

    @Override
    @Transactional
    void run(String... args) throws Exception {
        if (applicationArguments.containsOption('init') || System.getProperty('init') != null) {
            def profiles = Sets.newHashSet('default')
            profiles.addAll(environment.getActiveProfiles())
            initStaticList(profiles)
        }
    }

    /**
     * 初始化InitializeDomian注解的domain类
     * @see InitializeDomian
     */
    void initStaticList(Set profiles) {
        log.info("initialize system with StaticList profiles: $profiles")
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        Set doneSet = new HashSet()
        //遍历entity类，执行InitializeDomian初始化
        configurableApplicationContext.getBeansOfType(Datastore).each { String key, Datastore datastore ->
            datastore.getMappingContext().getPersistentEntities().each {
                initDomain(it.javaClass, doneSet, profiles)
            }
        }
    }

    /**
     * 根据InitializeDomian的profiles配置，决定是否进行初始化
     * @param domain
     * @param doneSet
     * @param profiles
     */
    void initDomain(Class domain, Set doneSet, Set profiles) {
        if (doneSet.contains(domain))
            return;
        InitializeDomian initializeDomian = domain.getAnnotation(InitializeDomian)
        //执行条件：1、是否注解InitializeDomian；2、profiles和命令行参数是否匹配（命令行如果未传profiles，默认为[default],也就是注解默认值）
        if (initializeDomian && initializeDomian.profiles().any { profiles.contains(it) }) {
            log.debug("$domain 初始化开始")
            //如果有依赖，先处理依赖类
            initializeDomian.depends().each { initDomain(it, doneSet, profiles) }
            //表数据为空
            if (!generalRepository.countAll(domain)) {
                Field initField = domain.getDeclaredField(initializeDomian.value())
                initField.setAccessible(true)
                initField.get(domain).each { generalRepository.saveEntity(it) }
            }
            log.debug("$domain 初始化完成")
        }
        doneSet.add(domain)
    }
}