package cn.xjbpm.ultron.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class ApplicationWebPathUtil {

	/**
	 * 获取应用访问的根路径
	 * @return
	 */
	public static String getAppRootPathUrl() {
		try {
			InetAddress localHost = Inet4Address.getLocalHost();
			String ip = localHost.getHostAddress();
			String port = StrUtil.nullToDefault(getPort(), "8080");
			if (StrUtil.isNotBlank(port)) {
				port = ":" + port;
			}
			String contextPath = StrUtil.nullToEmpty(getContextPath());
			contextPath = StrUtil.isBlank(contextPath) ? "/" : contextPath;
			return String.format("http://%s%s%s", ip, port, contextPath);
		}
		catch (UnknownHostException e) {
			throw new RuntimeException("无法获取本机IP地址：", e);
		}
	}

	private static String getContextPath() {
		return BeanContextUtil.getProperty("server.servlet.context-path");
	}

	private static String getPort() {
		return BeanContextUtil.getProperty("server.port");
	}

}
