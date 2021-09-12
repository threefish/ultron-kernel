package cn.xjbpm.common.util.classmeta;

import java.lang.reflect.Method;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/26
 */
public class MethodMeta {

	private Method method;

	private Integer lineNumber;

	private String methodStr;

	public MethodMeta(Method method, Integer lineNumber) {
		this.method = method;
		this.lineNumber = lineNumber;
	}

	public static String simpleMethodDesc(Method method) {
		return String.format("%s.%s(...)", method.getDeclaringClass().getSimpleName(), method.getName());
	}

	@Override
	public String toString() {
		if (methodStr == null) {
			if (lineNumber != null) {
				String className = method.getDeclaringClass().getSimpleName();
				String methodName = method.getName();
				methodStr = String.format("%s.%s(%s.java:%d)", className, methodName, className, lineNumber);
			}
			else {
				methodStr = simpleMethodDesc(method);
			}
		}
		return methodStr;
	}

}
