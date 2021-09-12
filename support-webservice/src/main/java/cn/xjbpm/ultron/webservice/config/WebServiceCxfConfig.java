package cn.xjbpm.ultron.webservice.config;

import cn.xjbpm.common.util.ApplicationWebPathUtil;
import cn.xjbpm.common.util.BeanContextUtil;
import cn.xjbpm.ultron.webservice.annotation.WebServiceProvider;
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
import org.springframework.util.ClassUtils;

import javax.xml.ws.Endpoint;
import java.util.List;

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
		final List beansByAnnotation = BeanContextUtil.getBeansOfAnnotation(WebServiceProvider.class);
		beansByAnnotation.forEach(webService -> {
			final Class<?> webServiceClass = webService.getClass();
			final WebServiceProvider annotation = ClassUtils.getUserClass(webServiceClass)
					.getAnnotation(WebServiceProvider.class);
			Endpoint endpoint = new EndpointImpl(springBus(), webService);
			endpoint.publish(annotation.serviceName());
			String beanName = annotation.serviceName() + "Endpoint";
			defaultListableBeanFactory.registerSingleton(beanName, endpoint);
			log.info("注册webservice服务,端点:{},服务地址：{}{}{}?wsdl", beanName, appRootPathUrl, WS_BASE_URL_PREFIX,
					annotation.serviceName());
		});
	}

}
