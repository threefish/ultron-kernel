package cn.xjbpm.ultron.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * 资源名称，用于描述接口功能
	 * @return
	 */
	String description() default "";

	/**
	 * 资源 key
	 * @return
	 */
	String key() default "";

	/**
	 * 时期，单位秒
	 * @return
	 */
	int period();

	/**
	 * 限制访问次数
	 * @return
	 */
	int count() default 1;

	/**
	 * 限流方式
	 * @return
	 */
	LimitType limitType() default LimitType.METHOD_NAME;

	/**
	 * 限流枚举
	 */
	enum LimitType {

		/**
		 * 方法名
		 */
		METHOD_NAME,
		/**
		 * IP地址
		 */
		IP,
		/**
		 * 表单数据
		 */
		FORM_DATA

	}

}
