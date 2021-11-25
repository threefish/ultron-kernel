package cn.xjbpm.ultron.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Getter
public class AutoRegisterMenuEvent extends ApplicationEvent {

	private final List<AutoMenuAuthDTO> autoRegisterMenuVO;

	public AutoRegisterMenuEvent(Object source, List<AutoMenuAuthDTO> autoRegisterMenuVO) {
		super(source);
		this.autoRegisterMenuVO = autoRegisterMenuVO;
	}

}
