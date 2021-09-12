package cn.xjbpm.ultron.id.generator.util;

import cn.xjbpm.ultron.id.generator.service.GlobalIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/19
 */
@Component
@Slf4j
public class GlobalIdServiceUtil implements ApplicationContextAware {

	private static GlobalIdService globalIdService;

	public static long nextLongId() {
		return globalIdService.nextLongId();
	}

	public static String nextStringId() {
		return globalIdService.nextStringId();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		globalIdService = applicationContext.getBean(GlobalIdService.class);
		Assert.notNull(globalIdService, "GlobalIdService is null");
		log.info("GlobalIdServiceUtil initialize successfully");
	}

}
