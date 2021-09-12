package cn.xjbpm.ultron.captcha.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/8
 */
@Data
@AllArgsConstructor
@Schema(description = "验证码对象")
public class CaptchaRespVO {

	/**
	 * 验证码唯一标识
	 */
	@Schema(description = "验证码唯一标识")
	private String key;

	/**
	 * 验证码base64图形数据
	 */
	@Schema(description = "验证码base64图形数据")
	private String base64data;

}
