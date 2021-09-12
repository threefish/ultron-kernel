package cn.xjbpm.ultron.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
public class RequestRepeatableWrapperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		// 忽略预检查请求
		if (request instanceof HttpServletRequest
				&& !RequestMethod.OPTIONS.name().equals(((HttpServletRequest) request).getMethod())) {
			requestWrapper = new RequestRepeatableWrapper((HttpServletRequest) request);
		}
		if (Objects.isNull(requestWrapper)) {
			// 包装request失败,将返回原来的request
			chain.doFilter(request, response);
		}
		else {
			chain.doFilter(requestWrapper, response);
		}
	}

}
