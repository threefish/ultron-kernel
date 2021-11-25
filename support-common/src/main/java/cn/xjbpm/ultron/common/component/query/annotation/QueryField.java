/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.common.component.query.annotation;

import java.lang.annotation.*;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface QueryField {

	/**
	 * 列名，为空默认当前字段名
	 * @return
	 */
	String column() default "";

	/**
	 * 查询方式
	 * @return
	 */
	QueryType type() default QueryType.EQ;

	/**
	 * 忽略null值和空值
	 * @return
	 */
	boolean ignoreNull() default true;

	/**
	 * 忽略空白字符串
	 * @return
	 */
	boolean ignoreBlankString() default true;

	/**
	 * 忽略空集合,或者空数组
	 * @return
	 */
	boolean ignoreCollectionEmpty() default true;

	/**
	 * 忽略零值
	 * @return
	 */
	boolean ignoreZero() default true;

}
