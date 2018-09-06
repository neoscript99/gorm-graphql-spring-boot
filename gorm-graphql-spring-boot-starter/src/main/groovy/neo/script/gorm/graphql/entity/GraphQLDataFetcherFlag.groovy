package neo.script.gorm.graphql.entity

import org.grails.datastore.gorm.GormEntity

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 注册自定义DataFetcher，覆盖Schema#generate中的默认值
 * @see org.grails.gorm.graphql.Schema#generate
 */
@interface GraphQLDataFetcherFlag {
    Class<GormEntity> entityClass();
}
