package cn.xjbpm.ultron.web.trace;

import cn.hutool.core.util.RandomUtil;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class TraceManager {

	public static String createTraceId() {
		return RandomUtil.randomString(10);
	}

}
