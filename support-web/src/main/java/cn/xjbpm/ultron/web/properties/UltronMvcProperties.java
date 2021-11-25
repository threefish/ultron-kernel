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
@ConfigurationProperties(prefix = UltronMvcProperties.PREFIX)
@Data
public class UltronMvcProperties {

	public static final String PREFIX = "ultron.mvc";

	/**
	 * 跨域配置
	 */
	private final Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();

	/**
	 * xss配置
	 */
	private Xss xss = new Xss();

	/**
	 * Swagger 配置
	 */
	private Swagger swagger = new Swagger();

	/**
	 * web接口安全
	 */
	private Boolean webApiSecurity = true;

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

	@Data
	public class Swagger {

		/**
		 * 设置要暴漏接口文档的配置环境
		 */
		List<String> profiles = new ArrayList<>();

		/**
		 * 项目文档标题
		 */
		String title;

		/**
		 * 项目文档描述
		 */
		String description;

		/**
		 * 版本号
		 */
		String version;

		/**
		 * 联系人姓名
		 */
		String contactName;

		/**
		 * 联系网址
		 */
		String contactUrl;

		/**
		 * 联系邮件
		 */
		String contactEmail;

	}

}
