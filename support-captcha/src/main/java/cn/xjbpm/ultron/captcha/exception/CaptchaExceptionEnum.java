package cn.xjbpm.ultron.captcha.exception;

import cn.xjbpm.common.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/8
 */
@AllArgsConstructor
@Getter
public enum CaptchaExceptionEnum implements ExceptionType {

	EXPIRED(1020001, "验证码过期"), WRONG(1020000, "验证码错误");

	private final Integer code;

	private final String description;

}
