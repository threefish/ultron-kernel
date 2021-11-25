package cn.xjbpm.ultron.web.autoconfigure;

import cn.xjbpm.ultron.web.filter.RequestRepeatableWrapperFilter;
import cn.xjbpm.ultron.web.filter.XssHttpServletRequestFilter;
import cn.xjbpm.ultron.web.interceptor.TraceHandlerInterceptor;
import cn.xjbpm.ultron.web.properties.UltronMvcProperties;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebMvcAutoConfig implements WebMvcConfigurer {

	private final TraceHandlerInterceptor traceHandlerInterceptor;

	private final UltronMvcProperties ultronMvcProperties;

	@Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
	private String defaultDatePattern;

	@Value("${spring.jackson.time-zone:GMT+8}")
	private String defaultTimeZone;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(traceHandlerInterceptor).addPathPatterns("/**").order(0);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(defaultDatePattern);
		final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.failOnEmptyBeans(false);
			builder.failOnUnknownProperties(false);
			builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

			// 生成JSON时,将Long转换成String,避免前端获取到数据即丢失精度
			builder.serializerByType(Long.class, ToStringSerializer.instance);
			builder.serializerByType(long.class, ToStringSerializer.instance);
			builder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
			builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
			builder.serializerByType(BigInteger.class, ToStringSerializer.instance);

			// 日期处理
			builder.timeZone(defaultTimeZone);
			builder.simpleDateFormat(defaultDatePattern);
			builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER));
			builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
			builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(TIME_FORMATTER));
			builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER));
			builder.serializerByType(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
			builder.serializerByType(LocalTime.class, new LocalTimeSerializer(TIME_FORMATTER));
		};
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setUseIsoFormat(true);
		registrar.registerFormatters(registry);
	}

	/**
	 * 跨域配置
	 */
	@Bean
	@ConditionalOnMissingBean(CorsFilter.class)
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.setCorsConfigurations(ultronMvcProperties.getCorsConfigurations());
		return new CorsFilter(source);
	}

	@Bean
	public FilterRegistrationBean requestRepeatableFilterRegistrationBean() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new RequestRepeatableWrapperFilter());
		registration.addUrlPatterns("/*");
		registration.setOrder(100);
		return registration;
	}

	@Bean
	public FilterRegistrationBean xssHttpServletRequestFilterRegistrationBean() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new XssHttpServletRequestFilter(ultronMvcProperties));
		registration.addUrlPatterns("/*");
		registration.setOrder(99);
		return registration;
	}

}
