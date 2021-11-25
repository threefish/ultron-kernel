package cn.xjbpm.ultron.web.advice;

import cn.hutool.core.util.BooleanUtil;
import cn.xjbpm.ultron.common.vo.JsonResultVO;
import cn.xjbpm.ultron.common.vo.PageRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class GlobalReponseDataAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * 表示数据需要包装，前端传参 "true", "yes", "y", "t", "ok", "1", "on" 都可以
	 */
	private static final String X_AUTO_DATA_WRAP = "x-auto-data-wrap";

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
			ServerHttpResponse serverHttpResponse) {
		if (object instanceof JsonResultVO) {
			return object;
		}
		if (object instanceof PageRespVO) {
			return object;
		}
		// string类型不好处理，请不要直接返回string类型
		if (object instanceof String) {
			log.error("请不要直接返回string类型!!! 请返回 JsonResultVO.sucess(\"字符串\")");
		}
		HttpHeaders headers = serverHttpRequest.getHeaders();
		String autoDataWrap = headers.getFirst(X_AUTO_DATA_WRAP);
		if (BooleanUtil.toBoolean(autoDataWrap)) {
			// 需要包装
			return JsonResultVO.sucess(object);
		}
		return object;
	}

}
