package cn.xjbpm.ultron.jpa.converter;

import cn.xjbpm.ultron.common.constant.CommonConstants;
import cn.xjbpm.ultron.common.exception.BusinessSilenceException;

import javax.persistence.AttributeConverter;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class LargeStringConverter implements AttributeConverter<String, byte[]> {

	@Override
	public byte[] convertToDatabaseColumn(String attribute) {
		try {
			return attribute == null ? null : attribute.getBytes(CommonConstants.UTF8);
		}
		catch (UnsupportedEncodingException var3) {
			throw new BusinessSilenceException("Unsupported charset.");
		}
	}

	@Override
	public String convertToEntityAttribute(byte[] dbData) {
		try {
			return (Objects.isNull(dbData) || dbData.length == 0) ? null : new String(dbData, CommonConstants.UTF8);
		}
		catch (UnsupportedEncodingException var3) {
			throw new BusinessSilenceException("Unsupported charset.");
		}
	}

}
