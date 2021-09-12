package cn.xjbpm.common.component.transaction;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@FunctionalInterface
public interface CallableEvent {

	/**
	 * 执行
	 */
	void exec();

}
