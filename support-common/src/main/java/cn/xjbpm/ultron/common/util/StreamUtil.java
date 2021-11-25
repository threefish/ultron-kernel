package cn.xjbpm.ultron.common.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class StreamUtil {

	/**
	 * 去重 https://stackoverflow.com/questions/23699371/java-8-distinct-by-property
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

}
