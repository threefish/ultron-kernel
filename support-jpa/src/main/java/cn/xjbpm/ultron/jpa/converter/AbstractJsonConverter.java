package cn.xjbpm.ultron.jpa.converter;

import cn.xjbpm.ultron.common.util.JsonUtil;

import javax.persistence.AttributeConverter;
import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public abstract class AbstractJsonConverter<T> implements AttributeConverter<T, String>, Serializable {

	public abstract Class<T> getTargetClass();

	@Override
	public String convertToDatabaseColumn(T attribute) {
		return JsonUtil.obj2Json(attribute);
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		return JsonUtil.json2Obj(dbData, this.getTargetClass());
	}

}
