/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TenantProperties.PREFIX)
public class TenantProperties {

	public static final String PREFIX = "ultron.tenant";

	/**
	 * 是否开启租户模式
	 */
	private Boolean enable = false;

	/**
	 * 需要排除的多租户的表
	 */
	private List<String> ignoreTables = Arrays.asList("ultron_code");

	/**
	 * 多租户字段名称
	 */
	private String column = "tenant_id";

	/**
	 * 排除不进行租户隔离的sql 样例全路径：cn.xjbpm.ultron.uaa.infrastructure.repository.UserMapper.getOne
	 */
	private List<String> ignoreSqls = new ArrayList<>();

}
