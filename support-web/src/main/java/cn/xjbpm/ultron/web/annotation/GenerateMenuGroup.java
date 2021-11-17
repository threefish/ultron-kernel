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
	 * el变量
	 * @return
	 */
	String[] args();

	/**
	 * 不方便创建的菜单放这里来
	 * @return
	 */
	GenerateMenu[] menus() default {};

}
