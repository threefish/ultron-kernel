package cn.xjbpm.ultron.redis.balancecall;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface BalancedRateRemoteCall {

	/**
	 * 推送一个事件，该事件持有要处理的数据
	 */
	void publishEvent(BalancedRateRemoteCallEvent balancedRateRemoteCallEvent);

}
