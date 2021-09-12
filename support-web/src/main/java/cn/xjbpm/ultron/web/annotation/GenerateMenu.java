package cn.xjbpm.ultron.web.annotation;

import cn.xjbpm.common.enums.MenuType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com 自动生成菜单或资源
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateMenu {

	/**
	 * 菜单名称
	 * @return
	 */
	String name();

	/**
	 * 菜单类型
	 * @return
	 */
	MenuType type() default MenuType.GROUP;

	/**
	 * 图标
	 * @return
	 */
	String icon() default "";

	/**
	 * 唯一自定义ID
	 * <p>
	 * @return
	 */
	String customizeId();

	/**
	 * 上级自定义ID
	 * <p>
	 * 留空表明自己是根节点
	 * @return
	 */
	String parentCustomizeId();

	/**
	 * 权限标识符
	 * <p>
	 * 留空标识无需权限验证,若当前方法上有 SecurityPermissions 则自动获取 SecurityPermissions 的第一个值
	 * @return
	 */
	String permissions() default "";

	/**
	 * 菜单排序
	 * @return
	 */
	int shortNo() default 0;

}
