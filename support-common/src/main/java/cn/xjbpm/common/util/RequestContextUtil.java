package cn.xjbpm.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/26
 */
@UtilityClass
public class RequestContextUtil {

	/**
	 * 获取当前request对象
	 * @return
	 */
	public static Optional<HttpServletRequest> getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (Objects.nonNull(requestAttributes)) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return Optional.ofNullable(request);
		}
		return Optional.empty();
	}

	/**
	 * 获取当前response对象
	 * @return
	 */
	public static Optional<HttpServletResponse> getCurrentResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (Objects.nonNull(requestAttributes)) {
			HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
			return Optional.ofNullable(response);
		}
		return Optional.empty();
	}

	/**
	 * 是否是Json请求
	 */
	public static boolean isJsonRequest() {
		Optional<HttpServletRequest> currentRequest = getCurrentRequest();
		if (currentRequest.isPresent()) {
			String contentType = currentRequest.get().getHeader(HttpHeaders.CONTENT_TYPE);
			return StrUtil.startWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
		}
		return false;
	}

	/**
	 * 是否是文件上传
	 */
	public static boolean isMultipart() {
		Optional<HttpServletRequest> currentRequest = getCurrentRequest();
		if (currentRequest.isPresent()) {
			String contentType = currentRequest.get().getHeader(HttpHeaders.CONTENT_TYPE);
			return contentType.toLowerCase().startsWith("multipart/");
		}
		return false;
	}

	/**
	 * 取得IP地址
	 * @return
	 */
	public static String getIpByReuqestHeader() {
		Optional<HttpServletRequest> currentRequest = getCurrentRequest();
		return IpUtil.getIP(currentRequest.orElse(null));
	}

}
