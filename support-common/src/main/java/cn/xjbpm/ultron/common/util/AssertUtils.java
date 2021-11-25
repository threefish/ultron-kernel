package cn.xjbpm.ultron.common.util;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.common.exception.BusinessSilenceException;
import cn.xjbpm.ultron.common.exception.ExceptionType;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/9/20 断言工具类
 */
@UtilityClass
public class AssertUtils {

	public static void isNotTrue(boolean expression, ExceptionType type) throws BusinessSilenceException {
		if (expression) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void isTrue(boolean expression, ExceptionType type) throws BusinessSilenceException {
		if (!expression) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void isNull(Object object, ExceptionType type) throws BusinessSilenceException {
		if (object != null) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void notNull(Object object, ExceptionType type) throws BusinessSilenceException {
		if (object == null) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void hasLength(String text, ExceptionType type) throws BusinessSilenceException {
		if (!StringUtils.hasLength(text)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void hasText(String text, ExceptionType type) throws BusinessSilenceException {
		if (!StringUtils.hasText(text)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void hasText(String text, ExceptionType type, Object... args) throws BusinessSilenceException {
		if (!StringUtils.hasText(text)) {
			throw new BusinessSilenceException(type, args);
		}
	}

	public static void doesNotContain(String textToSearch, String substring, ExceptionType type)
			throws BusinessSilenceException {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.contains(substring)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void notEmpty(Object[] array, ExceptionType type) throws BusinessSilenceException {
		if (ObjectUtils.isEmpty(array)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void noNullElements(Object[] array, ExceptionType type) throws BusinessSilenceException {
		if (array != null) {
			Object[] var2 = array;
			int var3 = array.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				Object element = var2[var4];
				if (element == null) {
					throw new BusinessSilenceException(type);
				}
			}
		}

	}

	public static void notEmpty(Collection<?> collection, ExceptionType type) throws BusinessSilenceException {
		if (CollectionUtils.isEmpty(collection)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void notEmpty(Map<?, ?> map, ExceptionType type) throws BusinessSilenceException {
		if (CollectionUtils.isEmpty(map)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void isTrue(boolean expression, ExceptionType type, String message) throws BusinessSilenceException {
		if (!expression) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void isTrue(boolean expression, ExceptionType type, Object... args) throws BusinessSilenceException {
		if (!expression) {
			throw new BusinessSilenceException(type, args);
		}
	}

	public static void isNull(Object object, ExceptionType type, String message) throws BusinessSilenceException {
		if (object != null) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void notNull(Object object, ExceptionType type, String message) throws BusinessSilenceException {
		if (object == null) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void notNull(Object object, ExceptionType type, Object... args) throws BusinessSilenceException {
		if (object == null) {
			throw new BusinessSilenceException(type, args);
		}
	}

	public static void hasLength(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StringUtils.hasLength(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void hasText(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StringUtils.hasText(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void isNotBlank(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StrUtil.isNotBlank(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void isNotBlank(String text, ExceptionType type) throws BusinessSilenceException {
		if (!StrUtil.isNotBlank(text)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void isBlank(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StrUtil.isBlank(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void isBlank(String text, ExceptionType type) throws BusinessSilenceException {
		if (!StrUtil.isBlank(text)) {
			throw new BusinessSilenceException(type);
		}
	}

	public static void isEmpty(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StrUtil.isEmpty(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void isNotEmpty(String text, ExceptionType type, String message) throws BusinessSilenceException {
		if (!StrUtil.isNotEmpty(text)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void doesNotContain(String textToSearch, String substring, ExceptionType type, String message)
			throws BusinessSilenceException {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.contains(substring)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void notEmpty(Object[] array, ExceptionType type, String message) throws BusinessSilenceException {
		if (ObjectUtils.isEmpty(array)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void noNullElements(Object[] array, ExceptionType type, String message)
			throws BusinessSilenceException {
		if (array != null) {
			Object[] var3 = array;
			int var4 = array.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				Object element = var3[var5];
				if (element == null) {
					throw new BusinessSilenceException(type, message);
				}
			}
		}

	}

	public static void notEmpty(Collection<?> collection, ExceptionType type, String message)
			throws BusinessSilenceException {
		if (CollectionUtils.isEmpty(collection)) {
			throw new BusinessSilenceException(type, message);
		}
	}

	public static void notEmpty(Map<?, ?> map, ExceptionType type, String message) throws BusinessSilenceException {
		if (CollectionUtils.isEmpty(map)) {
			throw new BusinessSilenceException(type, message);
		}
	}

}
