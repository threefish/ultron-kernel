package cn.xjbpm.ultron.common.exception;

import cn.hutool.core.util.ArrayUtil;
import lombok.NonNull;

/**
 * @author 黄川 huchuc@vip.qq.com 业务静默异常
 */
public class BusinessSilenceException extends RuntimeException implements BusinessException {

	private final String description;

	/**
	 * 默认为:业务异常
	 */
	private Integer code = CommonExceptionEnum.BUSINESS_EXCEPTION.getCode();

	public BusinessSilenceException(@NonNull String message) {
		super(message);
		this.description = message;
	}

	public BusinessSilenceException(@NonNull ExceptionType exceptionType, @NonNull String message) {
		super(message);
		this.code = exceptionType.getCode();
		this.description = message;
	}

	public BusinessSilenceException(@NonNull Integer exceptionCode, @NonNull String message) {
		super(message);
		this.code = exceptionCode;
		this.description = message;
	}

	public BusinessSilenceException(@NonNull Integer exceptionCode, @NonNull String message, Throwable cause) {
		super(message, cause);
		this.code = exceptionCode;
		this.description = message;
	}

	public BusinessSilenceException(@NonNull String message, Throwable cause) {
		super(message, cause);
		this.description = message;
	}

	public BusinessSilenceException(@NonNull ExceptionType type, Object... args) {
		this(type, ArrayUtil.isNotEmpty(args) ? String.format(type.getDescription(), args) : type.getDescription());
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

}
