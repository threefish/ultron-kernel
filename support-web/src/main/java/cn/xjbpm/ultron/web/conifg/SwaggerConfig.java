/*
 * Copyright ? 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.web.conifg;

import cn.hutool.core.collection.CollUtil;
import cn.xjbpm.ultron.web.constant.ConstantAuthoriztion;
import cn.xjbpm.ultron.web.properties.UltronMvcProperties;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@EnableOpenApi
@RequiredArgsConstructor
public class SwaggerConfig {

	private final UltronMvcProperties ultronMvcProperties;

	@Value("${spring.application.name:Ultron}")
	private String applicationName;

	@Bean
	public Docket initDocket(Environment env) {
		final UltronMvcProperties.Swagger swagger = ultronMvcProperties.getSwagger();
		Profiles profile = CollUtil.isEmpty(swagger.getProfiles()) ? Profiles.of("dev")
				: Profiles.of(swagger.getProfiles().toArray(new String[0]));
		boolean flag = env.acceptsProfiles(profile);
		return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).enable(flag).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any()).build()
				.globalRequestParameters(getGlobalRequestParameters());
	}

	/**
	 * 生成全局通用参数
	 * @return
	 */
	private List<RequestParameter> getGlobalRequestParameters() {
		List<RequestParameter> parameters = new ArrayList<>();
		parameters.add(new RequestParameterBuilder().name(ConstantAuthoriztion.AUTHORIZATION).description("jwt授权token")
				.in(ParameterType.HEADER).query(q -> q.model(m -> m.scalarModel(ScalarType.STRING))).required(false)
				.build());
		return parameters;
	}

	/**
	 * 基础信息
	 * @return
	 */
	private ApiInfo apiInfo() {
		final UltronMvcProperties.Swagger swagger = ultronMvcProperties.getSwagger();
		return new ApiInfoBuilder()
				.title(Optional.ofNullable(swagger.getTitle()).orElse(applicationName + "  Swagger3-接口文档"))
				.description(swagger.getDescription())
				.contact(new Contact(swagger.getContactName(), swagger.getContactUrl(), swagger.getContactEmail()))
				.version(swagger.getVersion()).build();
	}

}
