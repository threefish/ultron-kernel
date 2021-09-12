package cn.xjbpm.ultron.web.aspect;

import cn.hutool.crypto.digest.MD5;
import cn.xjbpm.common.exception.BusinessSilenceException;
import cn.xjbpm.common.exception.CommonExceptionEnum;
import cn.xjbpm.common.util.JsonUtil;
import cn.xjbpm.common.util.RequestContextUtil;
import cn.xjbpm.ultron.redis.util.RedisTemplateUtil;
import cn.xjbpm.ultron.web.annotation.NoRepeatSubmit;
import cn.xjbpm.ultron.web.constant.ConstantAuthoriztion;
import cn.xjbpm.ultron.web.filter.RequestRepeatableWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(70)
@Aspect
@Component
public class RepeatSubmitAspect {

	private static final String KEY_PREFIX = "noRepeatSubmit:";

	@Pointcut("@annotation(noRepeatSubmit)")
	public void pointCut(NoRepeatSubmit noRepeatSubmit) {
	}

	@Around("pointCut(noRepeatSubmit)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {
		HttpServletRequest request = RequestContextUtil.getCurrentRequest().orElse(null);
		Assert.notNull(request, "request can not null");
		// 获取请求数据组成唯一摘要
		String token = request.getHeader(ConstantAuthoriztion.AUTHORIZATION);
		String path = request.getServletPath();
		String queryString = request.getQueryString();
		String formDataDigestHex = "";
		if (request instanceof RequestRepeatableWrapper) {
			if (RequestContextUtil.isJsonRequest()) {
				formDataDigestHex = MD5.create().digestHex(request.getInputStream());
			}
			else {
				formDataDigestHex = JsonUtil.obj2Json(request.getParameterMap());
			}
		}
		String reidisKey = KEY_PREFIX
				+ MD5.create().digestHex(String.format("%s%s%s%s", token, path, queryString, formDataDigestHex));
		int lockSeconds = noRepeatSubmit.lockTime();
		if (RedisTemplateUtil.setIfAbsent(reidisKey, String.valueOf(Boolean.TRUE), lockSeconds,
				TimeUnit.MILLISECONDS)) {
			return proceedingJoinPoint.proceed();
		}
		else {
			// 短期内提交者的token、URL地址、表单数据是相同的，认为是重复提交的请求
			throw new BusinessSilenceException(CommonExceptionEnum.REPEAT_REQUEST);
		}
	}

}
