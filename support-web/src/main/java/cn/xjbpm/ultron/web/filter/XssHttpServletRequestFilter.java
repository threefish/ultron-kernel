package cn.xjbpm.ultron.web.filter;

import cn.xjbpm.ultron.web.properties.UltronMvcProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 防止XSS攻击的过滤器
 *
 * @author 黄川 huchuc@vip.qq.com
 */
public class XssHttpServletRequestFilter implements Filter {

	/**
	 * Ant匹配
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private final UltronMvcProperties.Xss xssProperties;

	public XssHttpServletRequestFilter(UltronMvcProperties xssProperties) {
		this.xssProperties = xssProperties.getXss();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// 忽略预检查请求
		if (handleExcludeURL(req) || RequestMethod.OPTIONS.name().equals(((HttpServletRequest) request).getMethod())) {
			chain.doFilter(request, response);
		}
		else {
			chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
		}
	}

	/**
	 * 处理排除 URL
	 * @param request
	 * @return
	 */
	private boolean handleExcludeURL(HttpServletRequest request) {
		if (xssProperties.isEnabled() == false) {
			return true;
		}
		// 请求路径检查
		String requestUri = request.getRequestURI();
		// 此路径是否不需要处理
		for (String exclude : xssProperties.getExcludes()) {
			if (ANT_PATH_MATCHER.match(exclude, requestUri)) {
				return true;
			}
		}
		return false;
	}

}
