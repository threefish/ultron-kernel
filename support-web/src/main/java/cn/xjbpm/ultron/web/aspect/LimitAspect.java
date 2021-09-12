package cn.xjbpm.ultron.web.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.xjbpm.common.exception.BusinessSilenceException;
import cn.xjbpm.common.exception.CommonExceptionEnum;
import cn.xjbpm.common.util.RequestContextUtil;
import cn.xjbpm.ultron.redis.util.RedisTemplateUtil;
import cn.xjbpm.ultron.web.annotation.Limit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static cn.xjbpm.common.constant.StringPool.COLON;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(90)
@Aspect
@Component
@RequiredArgsConstructor
public class LimitAspect {

	/**
	 * 限流脚本
	 */
	private final RedisScript<Long> timeWindowLimitRedisScript;

	@Pointcut("@annotation(limit)")
	public void pointCut(Limit limit) {
	}

	@Around("pointCut(limit)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, Limit limit) throws Throwable {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		Method signatureMethod = signature.getMethod();
		Limit.LimitType limitType = limit.limitType();
		String key = limit.key();
		if (StrUtil.isEmpty(key)) {
			if (limitType == Limit.LimitType.IP) {
				key = RequestContextUtil.getIp();
			}
			else if (limitType == Limit.LimitType.METHOD_NAME) {
				key = signatureMethod.getName();
			}
			else if (limitType == Limit.LimitType.FORM_DATA) {
				key = getParamsKey(proceedingJoinPoint.getArgs());
			}
		}
		String redisKey = StrUtil.join(COLON, "limit", limitType.name(), key);
		Long count = RedisTemplateUtil.execute(timeWindowLimitRedisScript, Arrays.asList(redisKey), limit.count(),
				limit.period());
		if (Objects.nonNull(count) && count.intValue() <= limit.count()) {
			if (log.isDebugEnabled()) {
				log.debug("第{}次访问[{}]接口，key为{}", count, redisKey, limit.description());
			}
			return proceedingJoinPoint.proceed();
		}
		else {
			throw new BusinessSilenceException(CommonExceptionEnum.LIMIT_REQUEST);
		}
	}

	/**
	 * 根据参数生成key
	 * @param args
	 * @return
	 */
	private String getParamsKey(Object[] args) {
		StringBuffer sb = new StringBuffer();
		for (Object arg : args) {
			sb.append("_" + arg.hashCode());
		}
		return SecureUtil.md5(sb.toString());
	}

}
