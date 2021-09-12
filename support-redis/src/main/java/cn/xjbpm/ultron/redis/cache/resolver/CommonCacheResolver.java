package cn.xjbpm.ultron.redis.cache.resolver;

import cn.xjbpm.ultron.redis.cache.annotation.CommonControllerCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class CommonCacheResolver extends AbstractCacheResolver {

	public static final String BEAN_NAME = "commonCacheResolver";

	private static final HashMap<Class, String> CACHE = new HashMap<>();

	public CommonCacheResolver(@Autowired CacheManager cacheManager) {
		super(cacheManager);
	}

	@Override
	protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		final Class<?> targetClass = context.getTarget().getClass();
		String cacheName = CACHE.get(targetClass);
		if (Objects.isNull(cacheName)) {
			final CommonControllerCache annotation = targetClass.getAnnotation(CommonControllerCache.class);
			Assert.notNull(annotation, String.format("类[%s]必须添加 @CommonControllerCache 注解！", targetClass));
			cacheName = annotation.cacheName();
			Assert.hasText(cacheName, String.format("类[%s] 上 CommonControllerCache cacheName 不能为空！", targetClass));
			CACHE.put(targetClass, cacheName);
		}
		return Arrays.asList(cacheName);
	}

}
