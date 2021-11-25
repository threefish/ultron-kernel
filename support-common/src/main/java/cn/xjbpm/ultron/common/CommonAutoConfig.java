package cn.xjbpm.ultron.common;

import cn.xjbpm.ultron.common.util.BeanContextUtil;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@AutoConfigureOrder
@ComponentScan(basePackageClasses = CommonAutoConfig.class)
public class CommonAutoConfig {

	@ConditionalOnMissingBean({ BeanContextUtil.class })
	@Bean
	public BeanContextUtil getSpringContextUtils() {
		return new BeanContextUtil();
	}

}
