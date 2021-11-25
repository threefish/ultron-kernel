package cn.xjbpm.ultron.captcha.service;

import cn.xjbpm.ultron.captcha.vo.CaptchaRespVO;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/8
 */
public interface CaptchaService {

	/**
	 * 生成验证码
	 * @return
	 */
	CaptchaRespVO createVerifyCode();

	/**
	 * 校验
	 * @param key 验证码key
	 * @param value 值
	 * @return
	 */
	boolean verify(String key, String value);

	/**
	 * 验证票据
	 * @param ticket
	 * @return
	 */
	boolean verifyTiket(String ticket);

	/**
	 * 校验并返回一张可以使用一次的凭据
	 * @param key
	 * @param value
	 * @return
	 */
	String verifyAndGetTiket(String key, String value);

}
