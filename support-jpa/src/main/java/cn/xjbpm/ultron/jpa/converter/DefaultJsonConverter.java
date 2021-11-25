package cn.xjbpm.ultron.jpa.converter;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class DefaultJsonConverter implements AttributeConverter<Object, String>, Serializable {

	private static final String DATA_TYPE = "dateType";

	private static final String DATA_JSON = "dataJson";

	private static final Logger logger = LoggerFactory.getLogger(DefaultJsonConverter.class);

	@Override
	public String convertToDatabaseColumn(Object attribute) {
		if (attribute == null) {
			return null;
		}
		else {
			String dataType = attribute.getClass().getName();
			String dataJson = JsonUtil.obj2Json(attribute);
			Map<String, String> data = new LinkedHashMap();
			data.put(DATA_TYPE, dataType);
			data.put(DATA_JSON, dataJson);
			return JsonUtil.obj2Json(data);
		}
	}

	@Override
	public Object convertToEntityAttribute(String dbData) {
		if (StrUtil.isBlank(dbData)) {
			return null;
		}
		else {
			Map dataInfo = JsonUtil.json2Obj(dbData, Map.class);
			Object dateType = dataInfo.get(DATA_TYPE);
			Object dataJson = dataInfo.get(DATA_JSON);

			try {
				Class<?> aClass = Class.forName(dateType.toString());
				return JsonUtil.json2Obj(dataJson.toString(), aClass);
			}
			catch (Exception var6) {
				logger.warn("Parse json data:{} failed.", dbData, var6);
				return null;
			}
		}
	}

}
