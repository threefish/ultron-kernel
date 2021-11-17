package cn.xjbpm.ultron.log.aspect;

import cn.xjbpm.common.constant.CommonConstants;
import cn.xjbpm.common.util.BeanContextUtil;
import cn.xjbpm.common.util.JsonUtil;
import cn.xjbpm.common.util.LoginUserUtil;
import cn.xjbpm.common.util.RequestContextUtil;
import cn.xjbpm.common.util.spel.SpELUtil;
import cn.xjbpm.ultron.log.annotation.OperationLog;
import cn.xjbpm.ultron.log.event.OperationLogEvent;
import cn.xjbpm.ultron.log.model.OperationLogModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
@Order(90)
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

	/**
	 * 可重复读的请求对象包装类
	 */
	private static final String REQUEST_REPEATABLE_WRAPPER_CLAZZ_NAME = "cn.xjbpm.ultron.web.filter.RequestRepeatableWrapper";

	@Pointcut("@annotation(operationLog)")
	public void pointCut(OperationLog operationLog) {
	}

	@Around("pointCut(operationLog)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, OperationLog operationLog) throws Throwable {
		final OperationLogModel operationLogModel = new OperationLogModel();
		final StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			operationLogModel.setSuccess(true);
			operationLogModel.setTraceId(MDC.get("traceId"));
			operationLogModel.setOperatorUserVO(LoginUserUtil.getOperator());
			operationLogModel.setLogType(operationLog.logType());
			operationLogModel.setOperateType(operationLog.operateType());
			if (operationLog.recordRequestData()) {
				Optional<HttpServletRequest> currentRequest = RequestContextUtil.getCurrentRequest();
				currentRequest.ifPresent(httpServletRequest -> {
					operationLogModel.setRequestUrl(String.format("[%s] %s", httpServletRequest.getMethod(),
							httpServletRequest.getRequestURI()));
					if (REQUEST_REPEATABLE_WRAPPER_CLAZZ_NAME.equals(httpServletRequest.getClass().getName())) {
						if (RequestContextUtil.isJsonRequest()) {
							try {
								operationLogModel.setRequestData(IOUtils.toString(httpServletRequest.getInputStream(),
										CommonConstants.UTF8_CHARSETS));
							}
							catch (IOException e) {
							}
						}
						else {
							operationLogModel.setRequestData(JsonUtil.obj2Json(httpServletRequest.getParameterMap()));
						}
					}
				});
			}
			try {
				final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
				final String value = SpELUtil.parseValueToString(signature.getMethod(), proceedingJoinPoint.getArgs(),
						operationLog.spel());
				operationLogModel.setText(value);
			}
			catch (Exception e) {
				operationLogModel.setText("日志拦截EL表达式有误，请及时修改代码：" + e.getMessage());
			}
			return proceedingJoinPoint.proceed();
		}
		catch (Exception e) {
			operationLogModel.setSuccess(false);
			operationLogModel.setErrMsg(e.getMessage());
			throw e;
		}
		finally {
			stopWatch.stop();
			operationLogModel.setConsumTime(stopWatch.getTotalTimeMillis());
			BeanContextUtil.publishAsyncEvent(new OperationLogEvent(this, operationLogModel));
			if (log.isInfoEnabled()) {
				log.info("操作日志 {}", JsonUtil.obj2Json(operationLogModel));
			}
		}
	}

}
