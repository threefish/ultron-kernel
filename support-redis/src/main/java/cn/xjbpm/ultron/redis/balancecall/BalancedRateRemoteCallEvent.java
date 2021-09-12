package cn.xjbpm.ultron.redis.balancecall;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface BalancedRateRemoteCallEvent {

	/**
	 * 获取 redis token key
	 *
	 */
	String getRedisTokenKey();

	/**
	 * 限制窗口时间
	 *
	 */
	long limitWindowTime();

	/**
	 * 窗口时间内允许的最大值
	 *
	 */
	long allowedMax();

	/**
	 * 执行调用
	 */
	void call();

}
