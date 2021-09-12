package cn.xjbpm.ultron.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.xjbpm.common.util.LoginUserUtil;
import cn.xjbpm.common.vo.OperatorUserVO;
import cn.xjbpm.ultron.web.constant.LogConstants;
import cn.xjbpm.ultron.web.trace.TraceManager;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com 参考 https://gitee.com/momentzhj/log-collection
 */
@Component
public class TraceHandlerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String traceId = request.getHeader(LogConstants.TRACE_ID);
		MDC.put(LogConstants.TRACE_ID, StrUtil.isNotEmpty(traceId) ? traceId : TraceManager.createTraceId());

		String traceUser = request.getHeader(LogConstants.TRACE_USER);
		if (StrUtil.isNotEmpty(traceUser)) {
			MDC.put(LogConstants.TRACE_USER, traceUser);
		}
		else {
			final Optional<OperatorUserVO> optionalOperator = LoginUserUtil.getOptionalOperator();
			optionalOperator
					.ifPresent(operatorUserVO -> MDC.put(LogConstants.TRACE_USER, operatorUserVO.getUserName()));
		}
		response.addHeader(LogConstants.TRACE_ID, MDC.get(LogConstants.TRACE_ID));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		MDC.remove(LogConstants.TRACE_ID);
		MDC.remove(LogConstants.TRACE_USER);
	}

}
