package cn.xjbpm.ultron.redis;

import cn.xjbpm.ultron.redis.cache.resolver.CommonCacheResolver;
import cn.xjbpm.ultron.redis.properties.UltronRedisProperties;
import cn.xjbpm.ultron.redis.serializer.Jackson2JsonRedisSerializer;
import cn.xjbpm.ultron.redis.util.RedisTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/7/28
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
@Slf4j
@ComponentScan(basePackageClasses = RedisAutoConfig.class)
public class RedisAutoConfig {

	private final LettuceConnectionFactory redisConnectionFactory;

	private final UltronRedisProperties ultronRedisProperties;

	/**
	 * springboot2.x 使用LettuceConnectionFactory 代替 RedisConnectionFactory
	 * application.yml配置基本信息后,springboot2.x RedisAutoConfiguration能够自动装配
	 * LettuceConnectionFactory 和 RedisConnectionFactory 及其 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Serializable> redisTemplate() {
		log.info("support-redis initialize");
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer();
		RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		RedisTemplateUtil.setRedisTemplate(redisTemplate);
		return redisTemplate;
	}

	/**
	 * 配置使用缓存注解的候配置
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate<String, Serializable> redisTemplate) {
		log.info("support-redis CacheManager initialize");
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer();
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				// 默认10分钟缓存
				.entryTtl(Duration.ofMinutes(ultronRedisProperties.getCaching().getExpiresMinutes()))
				// 设置统一的缓存前缀
				.computePrefixWith(
						cacheName -> String.format(ultronRedisProperties.getCaching().getPrefixFormat(), cacheName))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
		return RedisCacheManager.builder(redisTemplate.getConnectionFactory()).cacheDefaults(redisCacheConfiguration)
				.build();
	}

	@Bean
	@ConditionalOnBean(CacheManager.class)
	public CommonCacheResolver commonCacheResolver(CacheManager cacheManager) {
		return new CommonCacheResolver(cacheManager);
	}

	@Bean
	public ValueOperations<String, Serializable> valueOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	@Bean
	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	@Bean
	public ListOperations<String, Serializable> listOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean
	public SetOperations<String, Serializable> setOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	@Bean
	public ZSetOperations<String, Serializable> zSetOperations(RedisTemplate<String, Serializable> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

	@Bean
	public RedisScript<Long> tokenBucketLimitRedisScript() {
		return RedisScript.of(new ClassPathResource("scripts/redis/TokenBucketLimit.lua"), Long.class);
	}

	@Bean
	public RedisScript<Long> timeWindowLimitRedisScript() {
		return RedisScript.of(new ClassPathResource("scripts/redis/TimeWindowLimit.lua"), Long.class);
	}

}
