package cn.xjbpm.ultron.log.event;

import cn.xjbpm.ultron.log.model.OperationLogModel;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Getter
public class OperationLogEvent extends ApplicationEvent {

	private OperationLogModel operationLogModel;

	public OperationLogEvent(Object source, OperationLogModel operationLogModel) {
		super(source);
		this.operationLogModel = operationLogModel;
	}

}
