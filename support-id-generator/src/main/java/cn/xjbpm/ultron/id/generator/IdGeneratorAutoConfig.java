package cn.xjbpm.ultron.id.generator;

import cn.xjbpm.ultron.id.generator.service.GlobalIdService;
import cn.xjbpm.ultron.id.generator.service.impl.SnowflakeIdServiceImpl;
import cn.xjbpm.ultron.id.generator.util.GlobalIdServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/19
 */
@Slf4j
@Configuration
@Import(GlobalIdServiceUtil.class)
public class IdGeneratorAutoConfig {

	public IdGeneratorAutoConfig() {
		log.info("IdGeneratorAutoConfig init");
	}

	@Bean
	@ConditionalOnMissingBean(GlobalIdService.class)
	public GlobalIdService globalIdService() {
		return new SnowflakeIdServiceImpl();
	}

}
