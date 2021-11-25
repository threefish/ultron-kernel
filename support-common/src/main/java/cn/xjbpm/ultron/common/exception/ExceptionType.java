package cn.xjbpm.ultron.common.exception;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface ExceptionType {

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
