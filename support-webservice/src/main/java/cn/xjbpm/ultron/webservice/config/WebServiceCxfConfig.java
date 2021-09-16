package cn.xjbpm.ultron.webservice.config;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.common.util.ApplicationWebPathUtil;
import cn.xjbpm.common.util.BeanContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebServiceCxfConfig {

	private static final String WS_BASE_URL_PREFIX = "/webService/v1/";

	private final DefaultListableBeanFactory defaultListableBeanFactory;

	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}

	/**
	 * 注入servlet bean name不能dispatcherServlet 否则会覆盖dispatcherServlet
	 * @return
	 */
	@Bean(name = "cxfServlet")
	public ServletRegistrationBean cxfServlet() {
		return new ServletRegistrationBean(new CXFServlet(), WS_BASE_URL_PREFIX + "*");
	}

	/**
	 * 注册 webServiceEndpoint
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void registerWebService() {
		String appRootPathUrl = ApplicationWebPathUtil.getAppRootPathUrl();
		final List beansByAnnotation = BeanContextUtil.getBeansOfAnnotation(WebService.class);
		beansByAnnotation.forEach(webService -> {
			final Class<?> webServiceClass = webService.getClass();
			final Class<?> webServiceImplClass = ClassUtils.getUserClass(webServiceClass);
			final Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(webServiceImplClass);
			final List<Class<?>> webServiceClassInterfaces = Arrays.stream(allInterfacesForClass)
					.filter(i -> Objects.nonNull(i.getAnnotation(WebService.class))).collect(Collectors.toList());
			Assert.isTrue(webServiceClassInterfaces.size() == 1,
					"There should be only one WebService interface implemented  . by " + webServiceImplClass);
			final Class<?> webServiceInterfaceClass = webServiceClassInterfaces.get(0);
			final WebService interfaceAnnotation = webServiceInterfaceClass.getAnnotation(WebService.class);
			Assert.isTrue(StrUtil.isNotBlank(interfaceAnnotation.serviceName()),
					"WebService serviceName can't be null. by " + webServiceInterfaceClass);
			Assert.isTrue(StrUtil.isNotBlank(interfaceAnnotation.targetNamespace()),
					"WebService targetNamespace can't be null. by " + webServiceInterfaceClass);
			final WebService implAnnotation = webServiceImplClass.getAnnotation(WebService.class);
			Assert.isTrue(StrUtil.isNotBlank(implAnnotation.serviceName()),
					"WebService serviceName can't be null. by " + webServiceImplClass);
			Assert.isTrue(StrUtil.isNotBlank(implAnnotation.targetNamespace()),
					"WebService targetNamespace can't be null. by " + webServiceImplClass);
			Endpoint endpoint = new EndpointImpl(springBus(), webService);
			endpoint.publish(implAnnotation.serviceName());
			String beanName = implAnnotation.serviceName() + "Endpoint";
			defaultListableBeanFactory.registerSingleton(beanName, endpoint);
			log.info("注册webservice服务,端点:{},服务地址：{}{}{}?wsdl", beanName, appRootPathUrl, WS_BASE_URL_PREFIX,
					implAnnotation.serviceName());
		});
	}

}
