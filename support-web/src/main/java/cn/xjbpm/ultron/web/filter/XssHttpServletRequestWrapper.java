package cn.xjbpm.ultron.web.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.xjbpm.common.constant.CommonConstants;
import cn.xjbpm.common.util.RequestContextUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * XSS过滤处理
 *
 * @author ruoyi
 * @author 黄川 huchuc@vip.qq.com
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	private Map<String, String[]> parameterMap;

	/**
	 * @param request
	 */
	public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.parameterMap = request.getParameterMap();
		this.body = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (Objects.nonNull(values)) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				// 防xss攻击和过滤前后空格
				escapseValues[i] = HtmlUtil.cleanHtmlTag(values[i]).trim();
			}
			return escapseValues;
		}
		return super.getParameterValues(name);
	}

	/**
	 * 是否有内容
	 * @return
	 */
	public boolean hasBody() {
		return body.length > 0;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 是json类型请求
		if (RequestContextUtil.isJsonRequest()) {
			// 为空，直接返回
			String json = IOUtils.toString(body, CommonConstants.UTF8);
			if (StrUtil.isEmpty(json)) {
				return new RequestCatchingServletInputStream(new ByteArrayInputStream(body));
			}
			// xss过滤
			ByteArrayInputStream bais = new ByteArrayInputStream(
					HtmlUtil.cleanHtmlTag(json).trim().getBytes(StandardCharsets.UTF_8));
			return new RequestCatchingServletInputStream(bais);
		}
		return new RequestCatchingServletInputStream(new ByteArrayInputStream(body));
	}

	static class RequestCatchingServletInputStream extends ServletInputStream {

		ByteArrayInputStream bais;

		public RequestCatchingServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {

		}

		@Override
		public int read() {
			return bais.read();
		}

	}

}
