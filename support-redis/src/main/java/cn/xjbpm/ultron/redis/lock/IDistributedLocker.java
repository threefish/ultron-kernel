package cn.xjbpm.ultron.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/10/2 分布式锁
 * https://www.cnblogs.com/yangzhilong/p/7605807.html
 */
public interface IDistributedLocker {

	/**
	 * 加锁
	 * @param lockKey
	 * @return
	 */
	RLock lock(String lockKey);

	/**
	 * 带超时的锁
	 * @param lockKey
	 * @param timeout 超时时间 单位：秒
	 * @return
	 */
	RLock lock(String lockKey, int timeout);

	/**
	 * 带超时的锁
	 * @param lockKey
	 * @param unit 时间单位
	 * @param timeout 超时时间
	 * @return
	 */
	RLock lock(String lockKey, TimeUnit unit, int timeout);

	/**
	 * 尝试获取锁
	 * @param lockKey
	 * @param unit
	 * @param waitTime
	 * @param leaseTime
	 * @return 是否成功
	 */
	boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

	/**
	 * 释放锁
	 * @param lockKey
	 */
	void unlock(String lockKey);

	/**
	 * 释放锁
	 * @param lock
	 */
	void unlock(RLock lock);

	/**
	 * 设置 redissonClient
	 * @param redissonClient
	 */
	void setRedissonClient(RedissonClient redissonClient);

}
