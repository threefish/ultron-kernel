package cn.xjbpm.common.util;

import cn.xjbpm.common.function.Invoke;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/9/25
 */
@Slf4j
public class LogUtil {

	public static void trace(Invoke invoke) {
		if (log.isTraceEnabled()) {
			invoke.execute();
		}
	}

	public static void debug(Invoke invoke) {
		if (log.isDebugEnabled()) {
			invoke.execute();
		}
	}

	public static void info(Invoke invoke) {
		if (log.isInfoEnabled()) {
			invoke.execute();
		}
	}

	public static void warn(Invoke invoke) {
		if (log.isWarnEnabled()) {
			invoke.execute();
		}
	}

	public static void error(Invoke invoke) {
		if (log.isErrorEnabled()) {
			invoke.execute();
		}
	}

}
