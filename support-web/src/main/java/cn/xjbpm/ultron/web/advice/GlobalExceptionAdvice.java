/*
 * Copyright ? 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.web.advice;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.common.exception.BusinessSilenceException;
import cn.xjbpm.common.exception.CommonExceptionEnum;
import cn.xjbpm.common.exception.HttpStatusExceptionEnum;
import cn.xjbpm.common.vo.JsonResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

	/**
	 * 处理业务静默异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = BusinessSilenceException.class)
	@ResponseBody
	public JsonResultVO businessSilenceExceptionHandler(BusinessSilenceException ex) {
		if (log.isDebugEnabled()) {
			log.debug("静默异常不作任何记录:", ex);
		}
		return new JsonResultVO(ex.getCode(), getErrorMsg(ex));
	}

	/**
	 * 未读取到请求中的参数体，或参数格式无法解析
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	public JsonResultVO businessSilenceExceptionHandler(HttpMessageNotReadableException ex) {
		if (log.isDebugEnabled()) {
			log.debug("未读取到请求中的参数体，或参数格式无法解析: {}", ex);
		}
		return JsonResultVO.failed(HttpStatusExceptionEnum.PARAM_ERROR, getErrorMsg(ex));
	}

	/**
	 * 全局异常捕捉处理
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public JsonResultVO exceptionHandler(Exception ex) {
		if (ex instanceof BusinessSilenceException) {
			if (log.isDebugEnabled()) {
				log.debug("静默异常不作任何记录:", ex);
			}
			return new JsonResultVO(((BusinessSilenceException) ex).getCode(), getErrorMsg(ex));
		}
		else {
			log.error("全局异常", ex);
		}
		return new JsonResultVO(HttpStatusExceptionEnum.SERVER_ERROR.getCode(), getErrorMsg(ex));
	}

	/**
	 * 唯一约束引起的异常
	 * @param ex
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = DuplicateKeyException.class)
	public JsonResultVO exceptionHandler(DuplicateKeyException ex) {
		if (log.isDebugEnabled()) {
			log.debug("唯一约束引起的异常: {}", ex);
		}
		return JsonResultVO.failed(CommonExceptionEnum.DATA_DUPLICATION);
	}

	/**
	 * 数据完整性约束冲突异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = DataIntegrityViolationException.class)
	@ResponseBody
	public JsonResultVO illegalArgumentExceptionHandler(DataIntegrityViolationException ex) {
		if (log.isDebugEnabled()) {
			log.debug("数据完整性异常: {}", ex);
		}
		return JsonResultVO.failed(CommonExceptionEnum.DATA_INTEGRITY_VIOLATION_EXCEPTION);
	}

	/**
	 * 空指针异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = NullPointerException.class)
	@ResponseBody
	public JsonResultVO nullPointerExceptionHandler(NullPointerException ex) {
		log.error("空指针异常:", ex);
		return JsonResultVO.failed(HttpStatusExceptionEnum.SERVER_ERROR);
	}

	/**
	 * 非法参数异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public JsonResultVO illegalArgumentExceptionHandler(IllegalArgumentException ex) {
		if (log.isDebugEnabled()) {
			log.debug("非法参数异常:", ex);
		}
		return JsonResultVO.failed(HttpStatusExceptionEnum.PARAM_ERROR, getErrorMsg(ex));
	}

	/**
	 * spring 请求参数验证异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseBody
	public JsonResultVO missingServletRequestParameterException(MissingServletRequestParameterException ex) {
		if (log.isDebugEnabled()) {
			log.debug("请求参数验证异常: {}", ex);
		}
		return JsonResultVO.failed(HttpStatusExceptionEnum.PARAM_ERROR,
				String.format("参数[%s]是必须的！", ex.getParameterName()));
	}

	/**
	 * spring 方法参数转换不支持的异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentConversionNotSupportedException.class)
	@ResponseBody
	public JsonResultVO missingServletRequestParameterException(MethodArgumentConversionNotSupportedException ex) {
		if (log.isDebugEnabled()) {
			log.debug("spring方法参数转换不支持的异常: {}", ex);
		}
		MethodParameter parameter = ex.getParameter();
		return JsonResultVO.failed(HttpStatusExceptionEnum.PARAM_ERROR, String.format("参数[%s]类型为[%s],但传递的值为[%s]！",
				parameter.getParameterName(), parameter.getParameterType(), ex.getValue()));
	}

	/**
	 * spring 方法参数类型不匹配异常
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public JsonResultVO missingServletRequestParameterException(MethodArgumentTypeMismatchException ex) {
		if (log.isDebugEnabled()) {
			log.debug("spring方法参数转换不支持的异常: {}", ex);
		}
		MethodParameter parameter = ex.getParameter();
		return JsonResultVO.failed(HttpStatusExceptionEnum.PARAM_ERROR, String.format("参数[%s]类型为[%s],但传递的值为[%s]！",
				parameter.getParameterName(), parameter.getParameterType(), ex.getValue()));
	}

	/**
	 * 取得异常message
	 * @param ex
	 * @return
	 */
	private String getErrorMsg(Exception ex) {
		String errorMsg = ex.getMessage();
		if (ex instanceof BusinessSilenceException) {
			return ((BusinessSilenceException) ex).getDescription();
		}
		if (Objects.nonNull(ex.getCause()) && StrUtil.isNotBlank(ex.getCause().getMessage())) {
			errorMsg = ex.getCause().getMessage();
		}
		return errorMsg;
	}

}
