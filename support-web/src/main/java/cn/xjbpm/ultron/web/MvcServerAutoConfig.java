package cn.xjbpm.ultron.web;

import cn.xjbpm.common.util.ApplicationWebPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@Slf4j
@ComponentScan(basePackageClasses = MvcServerAutoConfig.class)
public class MvcServerAutoConfig {

	public MvcServerAutoConfig() {
		log.info("web-mvc initialize");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReadyEvent(ApplicationEvent event) {
		String appRootPathUrl = ApplicationWebPathUtil.getAppRootPathUrl();
		log.info("######################################################");
		log.info("                                                      ");
		log.info(appRootPathUrl);
		log.info("{}doc.html", appRootPathUrl);
		log.info("                                                      ");
		log.info("Application started                                   ");
		log.info("                                                      ");
		log.info("######################################################");
	}

	@EventListener(ContextClosedEvent.class)
	public void onFebsApplicationClosed(ApplicationEvent event) {
		log.info("                                                      ");
		log.info("######################################################");
		log.info("                                                      ");
		log.info("Application is closed                                 ");
		log.info("                                                      ");
		log.info("######################################################");
	}

}
