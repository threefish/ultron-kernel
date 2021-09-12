package cn.xjbpm.ultron.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com 权限控制注解 安全权限
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityPermissions {

	/**
	 * 需要的权限标识符，如果登录token里没有这个标识符则提示无权限访问
	 * <p>
	 * 推荐和 customizeId 保存一致
	 * @return
	 */
	String[] value();

	/**
	 * 默认任意全部满足一项权限就可以访问
	 * @return
	 */
	Logic logic() default Logic.OR;

	/**
	 * 逻辑判断处理
	 */
	enum Logic {

		/**
		 * 全部满足才能访问
		 */
		AND,
		/**
		 * 满足一项权限就可以访问
		 */
		OR

	}

}
