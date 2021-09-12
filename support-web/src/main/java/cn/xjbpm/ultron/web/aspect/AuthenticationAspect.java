package cn.xjbpm.ultron.web.aspect;

import cn.xjbpm.common.exception.HttpStatusExceptionEnum;
import cn.xjbpm.common.util.AssertUtils;
import cn.xjbpm.common.util.LoginUserUtil;
import cn.xjbpm.common.vo.OperatorUserVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(40)
@Aspect
@Component
public class AuthenticationAspect {

	@Pointcut("@within(cn.xjbpm.ultron.web.annotation.ManageApi) || @within(cn.xjbpm.ultron.web.annotation.MobileApi)")
	public void controller() {
	}

	@Pointcut("execution(public * *(..))")
	public void method() {
	}

	@Around("controller() && method()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Optional<OperatorUserVO> optionalOperator = LoginUserUtil.getOptionalOperator();
		AssertUtils.isTrue(optionalOperator.isPresent(), HttpStatusExceptionEnum.TOKEN_ERROR);
		return proceedingJoinPoint.proceed();
	}

}
