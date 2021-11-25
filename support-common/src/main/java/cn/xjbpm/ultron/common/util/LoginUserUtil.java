package cn.xjbpm.ultron.common.util;

import cn.xjbpm.ultron.common.context.CurrentOperationUserContextHolder;
import cn.xjbpm.ultron.common.vo.OperatorUserVO;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class LoginUserUtil {

	/**
	 * 获取当前登录的操作用户
	 * @return
	 */
	public static OperatorUserVO getOperator() {
		return CurrentOperationUserContextHolder.get();
	}

	/**
	 * 设置当前登录的操作用户
	 * @return
	 */
	public static void setOperator(OperatorUserVO operatorUser) {
		CurrentOperationUserContextHolder.set(operatorUser);
	}

	/**
	 * 获取当前登录的操作用户
	 * @return
	 */
	public static Optional<OperatorUserVO> getOptionalOperator() {
		return Optional.ofNullable(CurrentOperationUserContextHolder.get());
	}

	/**
	 * 销毁当前登录的操作用户
	 * @return
	 */
	public static void destory() {
		CurrentOperationUserContextHolder.destory();
	}

}
