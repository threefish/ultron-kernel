/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.autoconfigure;

import cn.xjbpm.ultron.mybaitsplus.component.generator.GlobalAssignIdentifierGenerator;
import cn.xjbpm.ultron.mybaitsplus.component.handler.BaseMetaObjectHandler;
import cn.xjbpm.ultron.mybaitsplus.component.interceptor.SqlLogInterceptor;
import cn.xjbpm.ultron.mybaitsplus.properties.TenantProperties;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.AllArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/12/2
 */
@Configuration
@AllArgsConstructor
public class MybatisPlusConfiguration {

	private final TenantProperties tenantProperties;

	private final TenantLineInnerInterceptor tenantLineInnerInterceptor;

	/**
	 * 自动填充数据
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public MetaObjectHandler mateMetaObjectHandler() {
		return new BaseMetaObjectHandler();
	}

	/**
	 * id生成器
	 */
	@Bean
	public IdentifierGenerator identifierGenerator() {
		return new GlobalAssignIdentifierGenerator();
	}

	/**
	 * 分页插件
	 */
	@Bean
	public Interceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		if (tenantProperties.getEnable()) {
			// 租户插件
			interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
		}
		// 分页插件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		// 防止全表更新与删除插件
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		return interceptor;
	}

	/**
	 * 自定义配置
	 */
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
			// 关闭 mybatis 默认的日志
			configuration.setLogPrefix("log.mybatis");
		};
	}

	/**
	 * 乐观锁拦截器
	 */
	@Bean
	public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	/**
	 * sql日志输出
	 * @return
	 */
	@Bean
	@Profile({ "dev", "test" })
	public Interceptor sqlLogInterceptor() {
		return new SqlLogInterceptor();
	}

}
