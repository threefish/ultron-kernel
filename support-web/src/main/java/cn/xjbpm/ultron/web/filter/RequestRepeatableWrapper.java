package cn.xjbpm.ultron.web.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com 用于包装原生request, 解决流读完一次就不能再次获取body数据的问题
 */
public class RequestRepeatableWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	private Map<String, String[]> parameterMap;

	public RequestRepeatableWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.parameterMap = request.getParameterMap();
		this.body = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	/**
	 * 是否有内容
	 * @return
	 */
	public boolean hasBody() {
		return body.length > 0;
	}

	@Override
	public ServletInputStream getInputStream() {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public int read() {
				return bais.read();
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

		};
	}

}
