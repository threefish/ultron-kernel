package cn.xjbpm.ultron.log.listener;

import cn.xjbpm.ultron.log.event.OperationLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/11/17
 */
@Component
@Slf4j
public class OperationLogEventListener implements ApplicationListener<OperationLogEvent> {

	/**
	 * //TODO 待扩展为统一收集
	 * @param event
	 */
	@Override
	public void onApplicationEvent(OperationLogEvent event) {
		log.info("收到操作日志:{}", event.getOperationLogModel());
	}

}
