package cn.xjbpm.ultron.jpa.auditor;

import cn.xjbpm.ultron.common.util.LoginUserUtil;
import cn.xjbpm.ultron.common.vo.OperatorUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Component
@Slf4j
public class CurrentAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		final OperatorUserVO operator = LoginUserUtil.getOperator();
		if (Objects.nonNull(operator)) {
			return Optional.ofNullable(operator.getUserName());
		}
		return Optional.empty();
	}

}
