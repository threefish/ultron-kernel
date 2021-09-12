package cn.xjbpm.ultron.web.annotation;

import java.lang.annotation.*;

/**
 * @author 黄川 huchuc@vip.qq.com 自定义注解防止表单重复提交
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatSubmit {

	/**
	 * 默认时间10秒
	 */
	int lockTime() default 10 * 1000;

}
