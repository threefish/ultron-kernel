package cn.xjbpm.ultron.common.vo;

import cn.xjbpm.ultron.common.constant.JsonResultConstant;
import cn.xjbpm.ultron.common.exception.BusinessException;
import cn.xjbpm.ultron.common.exception.ExceptionType;
import cn.xjbpm.ultron.common.exception.HttpStatusExceptionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@Schema(description = "统一数据返回结果集")
public class JsonResultVO<T> implements Serializable {

	@Schema(description = "服务器响应code", example = "200")
	protected Integer code;

	@Schema(description = "响应结果描述", example = "success")
	protected String message;

	@Schema(description = "响应结果", example = "[]|{}")
	protected T data;

	@Schema(description = "服务器当前时间戳", example = "2222222222222")
	protected Long ts;

	public JsonResultVO() {
	}

	public JsonResultVO(@NonNull Integer code, String message) {
		this.code = code;
		this.message = message;
		this.ts = System.currentTimeMillis();
	}

	public JsonResultVO(@NonNull Integer code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
		this.ts = System.currentTimeMillis();
	}

	/**
	 * 成功返回结果
	 * @param data 获取的数据
	 */
	public static <T> JsonResultVO<T> sucess(T data) {
		return new JsonResultVO(JsonResultConstant.SUCCESS_CODE, JsonResultConstant.SUCCESS_MSG, data);
	}

	/**
	 * 失败返回结果
	 * @param exceptionType 异常类型
	 */
	public static JsonResultVO failed(ExceptionType exceptionType) {
		return new JsonResultVO(exceptionType.getCode(), exceptionType.getDescription());
	}

	/**
	 * 失败返回结果
	 * @param exceptionType 异常类型
	 */
	public static JsonResultVO failed(ExceptionType exceptionType, Object... objects) {
		return new JsonResultVO(exceptionType.getCode(), String.format(exceptionType.getDescription(), objects));
	}

	/**
	 * 失败返回结果
	 * @param businessException 异常
	 */
	public static JsonResultVO failed(BusinessException businessException) {
		return new JsonResultVO(businessException.getCode(), businessException.getDescription());
	}

	/**
	 * 失败返回结果
	 * @param exception 异常
	 */
	public static JsonResultVO failed(Exception exception) {
		if (exception instanceof BusinessException) {
			return JsonResultVO.failed((BusinessException) exception);
		}
		return new JsonResultVO(HttpStatusExceptionEnum.SERVER_ERROR.getCode(), exception.getMessage());
	}

	/**
	 * 失败返回结果
	 * @param exceptionMessage 异常信息
	 */
	public static JsonResultVO failed(String exceptionMessage) {
		return new JsonResultVO(HttpStatusExceptionEnum.SERVER_ERROR.getCode(), exceptionMessage);
	}

	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return Objects.equals(JsonResultConstant.SUCCESS_CODE, this.code);
	}

}
