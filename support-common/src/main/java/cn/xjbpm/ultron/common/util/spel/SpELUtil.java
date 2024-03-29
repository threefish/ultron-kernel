package cn.xjbpm.ultron.common.util.spel;

import lombok.experimental.UtilityClass;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@SuppressWarnings("ALL")
@UtilityClass
public class SpELUtil {

	/**
	 * SpEL 解析器
	 */
	public static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 方法参数获取
	 */
	public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 计算
	 * @param spEL 表达式
	 * @param method 目标方法
	 * @param args 方法入参
	 * @return 解析后的字符串
	 */
	public static String parseValueToString(Method method, Object[] args, String spEL) {
		EvaluationContext context = getMethodContext(method, args);
		return parseValueToString(context, spEL);
	}

	/**
	 * 计算
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static String parseValueToString(Map<String, Object> context, String spEL) {
		return parseValueToString(getMapContext(context), spEL);
	}

	/**
	 * 计算
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static boolean parseValueToBoolean(Map<String, Object> context, String spEL) {
		return Boolean.parseBoolean(parseValueToString(getMapContext(context), spEL));
	}

	/**
	 * 执行
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static String parseValueToString(EvaluationContext context, String spEL) {
		return (String) parseValue(context, spEL, String.class);
	}

	/**
	 * 执行
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static Object parseValue(Map<String, Object> context, String spEL, Class<?> returnType) {
		return PARSER.parseExpression(spEL, ParserContext.TEMPLATE_EXPRESSION).getValue(getMapContext(context),
				returnType);
	}

	/**
	 * 执行
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static Object parseValue(EvaluationContext context, String spEL, Class<?> returnType) {
		return PARSER.parseExpression(spEL, ParserContext.TEMPLATE_EXPRESSION).getValue(context, returnType);
	}

	/**
	 * 构建上下文
	 * @param method
	 * @param args
	 * @return
	 */
	public static StandardEvaluationContext getMethodContext(Method method, Object[] args) {
		String[] paraNameArr = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
		Map<String, Object> context = new HashMap<>();
		// 把方法参数放入 SPEL 上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.put(paraNameArr[i], args[i]);
		}
		return getMapContext(context);
	}

	/**
	 * 构建上下文
	 * @param map
	 * @return
	 */
	public static StandardEvaluationContext getMapContext(Map<String, Object> map) {
		// SPEL 上下文
		StandardEvaluationContext context = new StandardEvaluationContext(map);
		context.addPropertyAccessor(new MapAccessor());
		context.setVariables(map);
		return context;
	}

	public static void main(String[] args) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("test", "测试");
		String expressionStr = "#{test} 哈喽";
		System.out.println(SpELUtil.parseValueToString(map, expressionStr));
	}

}
