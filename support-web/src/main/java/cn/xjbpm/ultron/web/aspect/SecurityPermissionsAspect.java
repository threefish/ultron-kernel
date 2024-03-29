package cn.xjbpm.ultron.web.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.xjbpm.ultron.common.exception.BusinessSilenceException;
import cn.xjbpm.ultron.common.exception.HttpStatusExceptionEnum;
import cn.xjbpm.ultron.common.util.LoginUserUtil;
import cn.xjbpm.ultron.common.util.spel.SpELUtil;
import cn.xjbpm.ultron.common.vo.OperatorUserVO;
import cn.xjbpm.ultron.web.annotation.GenerateMenuGroup;
import cn.xjbpm.ultron.web.annotation.SecurityPermissions;
import cn.xjbpm.ultron.web.properties.UltronMvcProperties;
import cn.xjbpm.ultron.web.util.GenerateMenuUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(50)
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityPermissionsAspect {

	private final UltronMvcProperties ultronMvcProperties;

	@Pointcut("@annotation(securityPermissions)")
	public void pointCut(SecurityPermissions securityPermissions) {
	}

	@Around("pointCut(securityPermissions)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, SecurityPermissions securityPermissions)
			throws Throwable {
		if (ultronMvcProperties.getWebApiSecurity()) {
			OperatorUserVO operator = LoginUserUtil.getOperator();
			if (Objects.nonNull(operator) && CollUtil.isNotEmpty(operator.getPermissions())) {
				Class<?> declaringType = proceedingJoinPoint.getTarget().getClass();
				Map<String, Object> stringObjectMap = GenerateMenuUtil
						.buildContext(declaringType.getAnnotation(GenerateMenuGroup.class));
				List<String> value = Stream.of(securityPermissions.value())
						.map(permission -> SpELUtil.parseValueToString(stringObjectMap, permission))
						.collect(Collectors.toList());
				boolean hasPermissions = false;
				if (securityPermissions.logic() == SecurityPermissions.Logic.OR) {
					hasPermissions = value.stream().anyMatch(operator.getPermissions()::contains);
				}
				else if (securityPermissions.logic() == SecurityPermissions.Logic.AND) {
					hasPermissions = value.stream().allMatch(operator.getPermissions()::contains);
				}
				if (hasPermissions) {
					return proceedingJoinPoint.proceed();
				}
			}
			throw new BusinessSilenceException(HttpStatusExceptionEnum.PERMISSION_DENY);
		}
		return proceedingJoinPoint.proceed();
	}

}
