package cn.xjbpm.ultron.common.util;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.common.constant.StringPool;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class IpUtil {

	private static final Pattern IPV4_PATTERN = Pattern
			.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
			.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	public static boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6StdAddress(final String input) {
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6HexCompressedAddress(final String input) {
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6Address(final String input) {
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
	}

	/**
	 * 取得IP地址
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
		if (Objects.isNull(request)) {
			return "";
		}
		final int length = 15;
		String ip = request.getHeader("X-Forwarded-For");
		if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
			if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (StrUtil.isEmpty(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		else if (ip.length() > length) {
			String[] ips = ip.split(StringPool.COMMA);
			for (int index = 0; index < ips.length; index++) {
				String strIp = ips[index];
				if (!(StringPool.UNKNOWN.equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		if (StrUtil.isBlank(ip)) {
			return "";
		}
		if (IpUtil.isIPv4Address(ip) || IpUtil.isIPv6Address(ip)) {
			return ip;
		}
		return "";
	}

}
