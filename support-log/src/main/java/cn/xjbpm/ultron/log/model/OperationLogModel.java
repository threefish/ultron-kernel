package cn.xjbpm.ultron.log.model;

import cn.xjbpm.common.enums.LogType;
import cn.xjbpm.common.enums.OperateType;
import cn.xjbpm.common.vo.OperatorUserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogModel {

	/**
	 * 操作人
	 */
	OperatorUserVO operatorUserVO;

	/**
	 * 追踪ID
	 */
	String traceId;

	/**
	 * 日志类型
	 * @return
	 */
	LogType logType;

	/**
	 * 操作类型
	 * @return
	 */
	OperateType operateType;

	/**
	 * 日志内容，可使用el表达式计算
	 * @return
	 */
	String text;

	/**
	 * 请求数据
	 */
	String requestData;

	/**
	 * 请求地址
	 */
	String requestUrl;

	/**
	 * 耗时
	 */
	Long consumTime;

	/**
	 * 是否执行成功
	 */
	boolean success;

	/**
	 * 请求地址
	 */
	String errMsg;

}
