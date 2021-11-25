package cn.xjbpm.ultron.redis.autoconfigure;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.redis.RedisAutoConfig;
import cn.xjbpm.ultron.redis.lock.IDistributedLocker;
import cn.xjbpm.ultron.redis.lock.RedissonDistributedLocker;
import cn.xjbpm.ultron.redis.properties.RedissonProperties;
import cn.xjbpm.ultron.redis.util.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/10/2
 */
@Configuration
@ConditionalOnClass({ RedisAutoConfig.class, RedissonProperties.class })
@AutoConfigureAfter(RedisAutoConfig.class)
public class RedissonAutoConfig {

	/**
	 * 哨兵模式自动装配
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "redisson.master-name")
	public RedissonClient redissonSentinel(@Autowired RedissonProperties redssionProperties) {
		Config config = new Config();
		SentinelServersConfig serverConfig = config.useSentinelServers()
				.addSentinelAddress(redssionProperties.getSentinelAddresses())
				.setMasterName(redssionProperties.getMasterName()).setTimeout(redssionProperties.getTimeout())
				.setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
				.setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());
		if (StrUtil.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		}
		return Redisson.create(config);
	}

	/**
	 * 单机模式自动装配
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "redisson.address")
	public RedissonClient redissonSingle(@Autowired RedissonProperties redssionProperties) {
		Config config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer().setAddress(redssionProperties.getAddress())
				.setTimeout(redssionProperties.getTimeout())
				.setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
				.setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());

		if (StrUtil.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		}
		return Redisson.create(config);
	}

	/**
	 * 装配locker类，并将实例注入到RedissLockUtil中
	 * @return
	 */
	@Bean
	@ConditionalOnBean(RedissonClient.class)
	public IDistributedLocker distributedLocker(RedissonClient redissonClient) {
		IDistributedLocker locker = new RedissonDistributedLocker();
		locker.setRedissonClient(redissonClient);
		RedissLockUtil.setLocker(locker);
		return locker;
	}

}
