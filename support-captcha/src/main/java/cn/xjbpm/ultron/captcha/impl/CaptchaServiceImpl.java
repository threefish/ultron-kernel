package cn.xjbpm.ultron.captcha.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.xjbpm.common.exception.BusinessSilenceException;
import cn.xjbpm.common.util.AssertUtils;
import cn.xjbpm.ultron.captcha.CaptchaService;
import cn.xjbpm.ultron.captcha.exception.CaptchaExceptionEnum;
import cn.xjbpm.ultron.captcha.vo.CaptchaRespVO;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/8
 */
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

	public static final String PREFIX = "Captcha:";

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public CaptchaRespVO createVerifyCode() {
		String key = RandomUtil.randomString(10);
		String redisKey = PREFIX.concat(key);
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
		String base64 = captcha.toBase64();
		String text = captcha.text();
		redisTemplate.opsForValue().set(redisKey, text, Duration.ofSeconds(30));
		return new CaptchaRespVO(key, base64);
	}

	@Override
	public boolean verify(String key, String value) {
		String redisKey = PREFIX.concat(key);
		String serializable = redisTemplate.opsForValue().get(redisKey);
		AssertUtils.hasText(serializable, CaptchaExceptionEnum.EXPIRED);
		if (Objects.nonNull(value) && Objects.nonNull(serializable)) {
			redisTemplate.delete(redisKey);
			return Objects.equals(value, serializable);
		}
		return false;
	}

	/**
	 * 票据是一次性的，直接删除成功表示有效，删除失败表示无效
	 * @param ticket
	 * @return
	 */
	@Override
	public boolean verifyTiket(String ticket) {
		// 加前缀是避免被人串改票据达到任意删除redis数据的可能性
		return redisTemplate.delete(PREFIX.concat(ticket));
	}

	@Override
	public String verifyAndGetTiket(String key, String value) {
		if (verify(key, value)) {
			String ticket = UUID.fastUUID().toString(true);
			// 这个key是一次性的，有效期20秒
			redisTemplate.opsForValue().set(PREFIX.concat(ticket), key, 30, TimeUnit.SECONDS);
			return ticket;
		}
		throw new BusinessSilenceException(CaptchaExceptionEnum.EXPIRED);
	}

}
