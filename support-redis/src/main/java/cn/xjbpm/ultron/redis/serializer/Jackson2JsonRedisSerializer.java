package cn.xjbpm.ultron.redis.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author 黄川 huchuc@vip.qq.com Jackson可参考该文章进行配置
 * https://blog.csdn.net/sdyy321/article/details/40298081
 */
public class Jackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final String CLASS_KEY = "@class";

	static {
		// 将类信息写入以便反序列化
		OBJECT_MAPPER.activateDefaultTypingAsProperty(OBJECT_MAPPER.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL, CLASS_KEY);
		// 为Null时不进行序列化
		OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 关闭默认序列化日期时采用timestamps输出
		OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// 忽略json中和javabean对应不上的字段
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// 注册java时间序列化
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
		OBJECT_MAPPER.registerModule(new Jdk8Module());
		OBJECT_MAPPER.registerModule(new ParameterNamesModule());
	}

	public Jackson2JsonRedisSerializer() {
		super(OBJECT_MAPPER);
	}

}
