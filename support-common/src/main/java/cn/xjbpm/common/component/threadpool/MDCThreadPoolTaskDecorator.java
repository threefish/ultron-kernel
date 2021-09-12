package cn.xjbpm.common.component.threadpool;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/12
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class MDCThreadPoolTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		// Right now: Web thread context !
		// (Grab the current thread MDC data)
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		return () -> {
			try {
				// Right now: @Async thread context !
				// (Restore the Web thread context's MDC data)
				if (contextMap != null) {
					MDC.setContextMap(contextMap);
				}
				runnable.run();
			}
			finally {
				MDC.clear();
			}
		};
	}

}
