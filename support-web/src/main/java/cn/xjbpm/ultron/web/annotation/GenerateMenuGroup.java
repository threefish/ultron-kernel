package cn.xjbpm.ultron.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateMenuGroup {

	/**
	 * customizeId 前缀 ，会影响使用el表达式的 GenerateMenu 和 SecurityPermissions 注解
	 * @return
	 */
	String el_prefix();

	/**
	 * 名称前缀
	 * @return
	 */
	String el_name_prefix();

	/**
	 * 不方便创建的菜单放这里来
	 * @return
	 */
	GenerateMenu[] menus() default {};

}
