package cn.xjbpm.ultron.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@AllArgsConstructor
@Getter
public enum CommonExceptionEnum implements ExceptionType {

	/**
	 * 兜底的业务异常码
	 */
	BUSINESS_EXCEPTION(1000000, "业务异常"),
	/**
	 * 重复请求
	 */
	REPEAT_REQUEST(1000001, "重复请求，请稍后再试"),
	/**
	 * 限流异常
	 */
	LIMIT_REQUEST(1000002, "当前操作太快被限流，请稍后再试"),
	/**
	 * 无符合条件的数据
	 */
	NOT_FOUNT_DATA(1000003, "无符合条件的数据"),
	/**
	 * 获取本地网络地址错误
	 */
	LOCAL_ADDRESS_NETWORK_ERROR(1000004, "获取本地网络地址错误"),
	/**
	 * 已有唯一键相同的数据存在，唯一约束异常
	 */
	DATA_DUPLICATION(1000005, "已有唯一键相同的数据存在"),
	/**
	 * 数据完整性约束冲突异常,违反了数据库约束
	 */
	DATA_INTEGRITY_VIOLATION_EXCEPTION(1000006, "数据完整性违规异常"),

	;

	private final Integer code;

	private final String description;

}
