package neo.script.gorm.general.initializer

import java.lang.annotation.*

/**
 * 根据静态list进行初始化
 * <p>满足如下条件进行初始化：
 * <ul>
 *     <li>如果domain类标记了InitializeDomian
 *     <li>且value对应的静态变量存在，并为list
 *     <li>数据库中不存在任何记录
 * </ul>
 * <p>depends代表依赖类，必须在这个类之后做初始化
 * <p>profiles对应spring.profiles.active所对应的参数，实现多套方案的初始化需求
 * Created by Neo on 2017-09-22.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface InitializeDomian {
    String value() default 'initList';

    Class[] depends() default [];

    String[] profiles() default ['default'];
}
