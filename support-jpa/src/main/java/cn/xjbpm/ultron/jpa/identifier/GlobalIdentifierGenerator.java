package cn.xjbpm.ultron.jpa.identifier;

import cn.hutool.core.lang.Snowflake;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class GlobalIdentifierGenerator implements IdentifierGenerator {

	public static final String CLASS_NAME = "GlobalIdentifierGenerator";

	/**
	 * 缓存下实体类ID映射关系
	 */
	private static final ConcurrentHashMap<Class<?>, Class<?>> ENTITY_TYPE_MAPPING = new ConcurrentHashMap<>();

	/**
	 * 非多实例情况下使用
	 */
	private final Snowflake snowflake = new Snowflake(1L, 1L);

	@Override
	public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object)
			throws HibernateException {
		Class originClass = object.getClass();
		Class idClass = this.computeIfAbsent(originClass);
		if (idClass == String.class) {
			return snowflake.nextIdStr();
		}
		if (idClass == Long.class) {
			return snowflake.nextId();
		}
		throw new RuntimeException(
				String.format("全局主键ID仅支持[String、Long],不支持当前类型:%s#ID#%s", object.getClass(), idClass));
	}

	/**
	 * 获取实体类ID类型，不存在就存入缓存
	 * @param originClass 源
	 * @return 返回实体类ID类型
	 */
	public Class computeIfAbsent(Class<?> originClass) {
		return ENTITY_TYPE_MAPPING.computeIfAbsent(originClass, aClass -> {
			Type genericInterface = originClass.getGenericSuperclass();
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				if (actualTypeArguments.length > 0) {
					return (Class) actualTypeArguments[0];
				}
			}
			return null;
		});
	}

}
