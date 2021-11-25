package cn.xjbpm.ultron.web.aspect;

import cn.xjbpm.ultron.common.util.RequestContextUtil;
import cn.xjbpm.ultron.common.util.classmeta.ClassMeta;
import cn.xjbpm.ultron.common.util.classmeta.ClassMetaReader;
import cn.xjbpm.ultron.common.util.classmeta.MethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(0)
@Aspect
@Component
public class ControllerLogAspect {

	private static final Map<String, MethodMeta> CATCH_MAP = new HashMap();

	@Pointcut("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && execution(public * *(..))")
	public void controller() {
	}

	@Pointcut("execution(* cn.xjbpm.ultron.web.controller.CommonController.*(..))")
	public void method() {
	}

	@Around("controller() || method()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isDebugEnabled()) {
			Optional<HttpServletRequest> currentRequest = RequestContextUtil.getCurrentRequest();
			if (currentRequest.isPresent()) {
				StopWatch stopWatch = new StopWatch();
				HttpServletRequest request = currentRequest.get();
				MethodSignature signature = (MethodSignature) joinPoint.getSignature();
				Method method = signature.getMethod();
				String key = ClassMetaReader.getKey(method);
				MethodMeta methodMeta = getMethodMeta(key, method, signature.getDeclaringType());
				try {
					log.debug("Found [{}] path={} : {}", request.getMethod(), request.getRequestURI(), methodMeta);
					stopWatch.start();
					return joinPoint.proceed();
				}
				finally {
					stopWatch.stop();
					log.debug("time:{}ms for [{}] path={} ", stopWatch.getTotalTimeMillis(), request.getMethod(),
							request.getRequestURI());
				}
			}
		}
		return joinPoint.proceed();
	}

	private MethodMeta getMethodMeta(String key, Method method, Class<?> clazz) {
		MethodMeta methodMeta = CATCH_MAP.get(key);
		if (Objects.isNull(methodMeta)) {
			ClassMeta classMeta = ClassMetaReader.buildWithClass(clazz);
			int lineNumber = classMeta.methodLines.get(key);
			methodMeta = new MethodMeta(method, lineNumber);
			CATCH_MAP.put(key, methodMeta);
		}
		return methodMeta;
	}

}
