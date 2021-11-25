package cn.xjbpm.ultron.web.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.xjbpm.ultron.common.exception.BusinessSilenceException;
import cn.xjbpm.ultron.common.exception.HttpStatusExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(80)
@Aspect
@Component
public class RequestParameterValidationAspect {

	private LocalValidatorFactoryBean localValidatorFactoryBean;

	@Autowired
	public void setLocalValidatorFactoryBean(LocalValidatorFactoryBean localValidatorFactoryBean) {
		this.localValidatorFactoryBean = localValidatorFactoryBean;
	}

	public RequestParameterValidationAspect() {
		log.info("init RequestParameterValidationAspect");
	}

	@Pointcut("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)) && execution(public * *(..))")
	public void controller() {
	}

	@Pointcut("execution(* cn.xjbpm.ultron.web.controller.CommonController.*(..))")
	public void method() {
	}

	@Before("controller() && method()")
	public void requestParameterValidate(JoinPoint joinPoint) {
		Map<String, String> errors = new LinkedHashMap();
		this.paramValidate(joinPoint, errors);
		if (!errors.isEmpty()) {
			throw new BusinessSilenceException(HttpStatusExceptionEnum.PARAM_ERROR, StrUtil.join(",", errors.values()));
		}
	}

	/**
	 * 入参校验
	 * @param joinPoint
	 * @param errors
	 */
	public void paramValidate(JoinPoint joinPoint, Map<String, String> errors) {
		Object[] params = joinPoint.getArgs();
		final Object target = joinPoint.getTarget();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		if (ArrayUtil.isNotEmpty(params)) {
			Set<ConstraintViolation<Object>> constraintViolations = CollUtil.newHashSet(
					this.localValidatorFactoryBean.forExecutables().validateParameters(target, method, params));
			if (CollUtil.isEmpty(constraintViolations)) {
				Arrays.stream(params).filter(Objects::nonNull).forEach(param -> {
					try {
						constraintViolations.addAll(this.localValidatorFactoryBean.validate(param));
					}
					catch (Exception e) {
						log.error("未覆盖的验证处理类，异常类型:{}", e.getClass(), e);
					}
				});
			}
			if (CollUtil.isNotEmpty(constraintViolations)) {
				constraintViolations.stream().forEach(constraintViolation -> {
					PathImpl propertyPath = (PathImpl) constraintViolation.getPropertyPath();
					String parameterName = propertyPath.toString();
					if (propertyPath.getLeafNode().getKind() == ElementKind.PARAMETER) {
						int parameterIndex = propertyPath.getLeafNode().getParameterIndex();
						parameterName = signature.getParameterNames()[parameterIndex];
					}
					errors.put(parameterName, constraintViolation.getMessage());
				});
			}
		}
	}

}
