package cn.xjbpm.ultron.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/19
 */
@Configuration
@ConfigurationProperties(prefix = UltronRedisProperties.PREFIX)
@Data
public class UltronRedisProperties {

	public static final String PREFIX = "ultron.redis";

	private Caching caching = new Caching();

	@Data
	public class Caching {

		/**
		 * spring缓存，默认失效时间
		 */
		private int expiresMinutes = 10;

		/**
		 * 缓存前缀
		 */
		private String prefixFormat = "SpringCache:%s:";

	}

}
