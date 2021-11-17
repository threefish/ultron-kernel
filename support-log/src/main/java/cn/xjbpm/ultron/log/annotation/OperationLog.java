package cn.xjbpm.ultron.log.annotation;

import cn.xjbpm.common.enums.LogType;
import cn.xjbpm.common.enums.OperateType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface OperationLog {

	/**
	 * 日志类型
	 * @return
	 */
	LogType logType() default LogType.OPERATION_LOG;

	/**
	 * 操作类型
	 * @return
	 */
	OperateType operateType();

	/**
	 * 日志内容，可使用el表达式计算
	 * @return
	 */
	String text() default "";

	/**
	 * 记录请求数据
	 * @return
	 */
	boolean recordRequestData() default false;

}
