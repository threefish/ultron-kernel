package cn.xjbpm.common.exception;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface BusinessException {

	/**
	 * 获取异常编码
	 * @return
	 */
	Integer getCode();

	/**
	 * 异常摘要信息
	 * @return
	 */
	String getDescription();

}
