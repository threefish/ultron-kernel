package cn.xjbpm.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@AllArgsConstructor
@Getter
public enum HttpStatusExceptionEnum implements ExceptionType {

	/**
	 * 请求参数错误
	 */
	PARAM_ERROR(400, "请求参数错误:%s"),
	/**
	 * 令牌校验失败
	 */
	TOKEN_ERROR(401, "令牌校验失败,请重新登录"),
	/**
	 * 无权访问
	 */
	PERMISSION_DENY(403, "权限不足"),
	/**
	 * 404
	 */
	RESOURCE_NOT_FOUND(404, "资源未找到 %s"),
	/**
	 * 服务器繁忙，请稍后重试
	 */
	SERVER_ERROR(500, "服务器繁忙，请稍后重试");

	private final Integer code;

	private final String description;

}
