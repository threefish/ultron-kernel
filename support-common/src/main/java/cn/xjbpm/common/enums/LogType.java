package cn.xjbpm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Getter
@AllArgsConstructor
public enum LogType {

	/**
	 * 操作日志
	 */
	OPERATION_LOG("操作日志"),
	/**
	 * 登陆日志
	 */
	LOGIN_LOG("登陆日志"),
	/**
	 * 重要操作日志
	 */
	IMPORTANT_OPERATION_LOG("重要操作日志");

	String description;

}
