package cn.xjbpm.ultron.common.context;

import cn.xjbpm.ultron.common.vo.OperatorUserVO;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 当前线程操作人员
 *
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class CurrentOperationUserContextHolder {

	/**
	 * 支持父子线程之间的数据传递
	 */
	private static final ThreadLocal<OperatorUserVO> CURRENT_OPERATOR_DTO_THREAD_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 获取当前线程操作者
	 * @return
	 */
	public static OperatorUserVO get() {
		return CURRENT_OPERATOR_DTO_THREAD_LOCAL.get();
	}

	/**
	 * 设置当前线程操作者
	 * @param currentOperatorDTO
	 */
	public static void set(OperatorUserVO currentOperatorDTO) {
		CURRENT_OPERATOR_DTO_THREAD_LOCAL.set(currentOperatorDTO);
	}

	/**
	 * 销毁线程变量
	 */
	public static void destory() {
		CURRENT_OPERATOR_DTO_THREAD_LOCAL.remove();
	}

}
