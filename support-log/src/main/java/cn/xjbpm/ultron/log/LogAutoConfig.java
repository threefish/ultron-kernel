package cn.xjbpm.ultron.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@Slf4j
@ComponentScan(basePackageClasses = LogAutoConfig.class)
public class LogAutoConfig {

	public LogAutoConfig() {
		log.info("support-log initialize");
	}

}
