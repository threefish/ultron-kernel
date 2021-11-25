/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.autoconfigure;

import cn.xjbpm.ultron.common.context.TenantContextHolder;
import cn.xjbpm.ultron.mybaitsplus.properties.TenantProperties;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore(MybatisPlusConfiguration.class)
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfiguration {

	private final TenantProperties tenantProperties;

	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		return new TenantLineInnerInterceptor(new TenantLineHandler() {
			/**
			 * 获取租户ID
			 * @return Expression
			 */
			@Override
			public Expression getTenantId() {
				Optional<String> tenantId = TenantContextHolder.getTenantId();
				return tenantId.isPresent() ? new StringValue(tenantId.get()) : new NullValue();
			}

			/**
			 * 获取多租户的字段名
			 * @return String
			 */
			@Override
			public String getTenantIdColumn() {
				return tenantProperties.getColumn();
			}

			/**
			 * 过滤不需要根据租户隔离的表 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
			 * @param tableName 表名
			 */
			@Override
			public boolean ignoreTable(String tableName) {
				return tenantProperties.getIgnoreTables().stream().anyMatch(tableName::equalsIgnoreCase);
			}
		});
	}

}
