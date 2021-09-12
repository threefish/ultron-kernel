package cn.xjbpm.ultron.redis.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com 继承 CommonController 的才需要设置这个注解 或者使用了 cacheResolver 指定了
 * CommonCacheResolver.BEAN_NAME
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonControllerCache {

	String cacheName();

}
