package cn.xjbpm.ultron.web.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "ultron.mvc")
@Data
public class UltronMvcProperties {

	/**
	 * 跨域配置
	 */
	private final Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();

	/**
	 * xss配置
	 */
	private Xss xss = new Xss();

	@Data
	public class Xss {

		/**
		 * 是否启用xss过滤
		 */
		boolean enabled;

		/**
		 * 排除的url规则
		 */
		List<String> excludes = new ArrayList<>();

	}

}
