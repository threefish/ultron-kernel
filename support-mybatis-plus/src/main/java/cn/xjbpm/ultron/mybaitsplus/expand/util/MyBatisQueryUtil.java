/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.expand.util;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.common.component.query.annotation.QueryField;
import cn.xjbpm.common.component.query.annotation.QuerySorts;
import cn.xjbpm.common.component.query.annotation.QueryType;
import cn.xjbpm.common.vo.OrderByVO;
import cn.xjbpm.common.vo.PageReqVO;
import cn.xjbpm.ultron.mybaitsplus.expand.criteria.Condition;
import cn.xjbpm.ultron.mybaitsplus.expand.criteria.OperatorType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class MyBatisQueryUtil {

	static final List<Class<?>> NUMBER_CLASS = Arrays.asList(int.class, float.class, long.class, Long.class,
			Float.class, Integer.class);

	/**
	 * 生成查询链
	 * @param queryWrapper
	 * @param value
	 * @return
	 */
	public static QueryChainWrapper toQueryChainWrapper(QueryChainWrapper queryWrapper, Object value) {
		final Class<?> queryVoClass = ClassUtils.getUserClass(value);
		List<Condition> conditions = getCondition(queryVoClass, value);
		// 加入条件
		for (Condition condition : conditions) {
			condition.getOperator().build(queryWrapper, condition.getColumn(), condition.getValue());
		}
		List<OrderByVO> orders = Collections.emptyList();
		if (value instanceof PageReqVO) {
			orders = ((PageReqVO) value).getOrders();
		}
		// 获取前端传递的排序字段,前端排序字段需要在白名单中
		QuerySorts querySorts = queryVoClass.getAnnotation(QuerySorts.class);
		if (Objects.nonNull(querySorts)) {
			String[] whitelist = querySorts.whitelist();
			// 添加排序规则
			for (OrderByVO order : orders) {
				Arrays.stream(whitelist).filter(s -> s.equals(order.getField())).findAny()
						.ifPresent(s -> queryWrapper.orderBy(true, order.getType().getSort() == Sort.Direction.ASC,
								StringUtils.camelToUnderline(order.getField())));
			}
		}
		return queryWrapper;
	}

	/**
	 * 获取查询条件
	 * @param value
	 * @return
	 */
	public static List<Condition> getCondition(Class<?> queryVoClass, Object value) {
		List<Condition> conditions = new ArrayList<>();
		final Field[] fields = queryVoClass.getDeclaredFields();
		for (Field field : fields) {
			QueryField annotation = field.getAnnotation(QueryField.class);
			if (Objects.nonNull(annotation)) {
				field.setAccessible(true);
				Object fieldValue = ReflectionUtils.getField(field, value);
				if (checkCondition(annotation, field, fieldValue)) {
					QueryType type = annotation.type();
					OperatorType operatorType = OperatorType.valueOf(type.name());
					Assert.notNull(operatorType, "暂时不支持的类型");
					conditions.add(Condition.builder().column(getQueryFieldName(annotation, field))
							.operator(operatorType).value(fieldValue).build());
				}
			}
		}
		return conditions;
	}

	/**
	 * 没有设置name属性时，默认为该字段的名称
	 * @param field
	 * @return
	 */
	private static String getQueryFieldName(QueryField annotation, Field field) {
		String queryFieldValue = annotation.column();
		if (StringUtils.isBlank(queryFieldValue)) {
			queryFieldValue = field.getName();
		}
		return StringUtils.camelToUnderline(queryFieldValue);
	}

	/**
	 * 获取字段名
	 * @param camelToUnderline
	 * @param name
	 * @return
	 */
	private static String getColumnName(boolean camelToUnderline, String name) {
		return camelToUnderline ? StringUtils.camelToUnderline(name) : name;
	}

	/**
	 * 检查是否符合条件
	 * @param annotation
	 * @param field
	 * @return
	 */
	private static boolean checkCondition(QueryField annotation, Field field, Object value) {
		if (annotation.ignoreNull() && Objects.isNull(value)) {
			// 忽略null值,为null就返回false
			return false;
		}
		if (annotation.ignoreBlankString() && value instanceof String && StrUtil.isBlank(((String) value))) {
			// 忽略null值,为null就返回false
			return false;
		}
		if (annotation.ignoreZero() && isZero(field.getType(), value)) {
			// 忽略零值
			return false;
		}
		// 忽略空集合
		return !annotation.ignoreCollectionEmpty() || !isCollectionEmpty(field.getType(), value);
	}

	/**
	 * 是空集合或者空数组
	 * @param type
	 * @param value
	 * @return
	 */
	private boolean isCollectionEmpty(Class<?> type, Object value) {
		if (Objects.isNull(value)) {
			return true;
		}
		if (Collection.class.isAssignableFrom(type)) {
			return value == null && CollectionUtils.isEmpty(((Collection) value));
		}
		else if (type.isArray()) {
			return value == null && ((Object[]) value).length == 0;
		}
		return false;
	}

	/**
	 * 是零
	 * @param value
	 * @return
	 */
	private boolean isZero(Class<?> type, Object value) {
		if (NUMBER_CLASS.contains(type)) {
			return "0".equals(value.toString());
		}
		return false;
	}

}
