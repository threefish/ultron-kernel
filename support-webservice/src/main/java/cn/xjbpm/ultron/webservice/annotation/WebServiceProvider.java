package cn.xjbpm.ultron.webservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com 对外提供的webService需要添加这个注解服务
 * @see cn.xjbpm.ultron.webservice.config.WebServiceCxfConfig#registerWebService
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebServiceProvider {

	String serviceName();

}
