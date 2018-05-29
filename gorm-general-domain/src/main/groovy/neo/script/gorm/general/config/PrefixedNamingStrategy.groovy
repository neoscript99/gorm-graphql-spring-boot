package neo.script.gorm.general.config

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
class PrefixedNamingStrategy extends ImprovedNamingStrategy {
    /**
     * 类名生成表名
     * 取最后一层包名作为表名前缀
     * 如：a.b.c.User -->  c_user
     *
     * @param className
     * @return
     */
    @Override
    public String classToTableName(String className) {
        def names = className.split("\\.");

        return addUnderscores(names.takeRight(2).join('_'));
    }

}
