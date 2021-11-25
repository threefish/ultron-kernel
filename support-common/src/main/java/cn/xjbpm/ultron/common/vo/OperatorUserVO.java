package cn.xjbpm.ultron.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperatorUserVO {

	/**
	 * 用户ID
	 */
	private Long id;

	/**
	 * 用户姓名
	 */
	private String name;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 工号
	 */
	private String jobNumber;

	/**
	 * 主岗位ID
	 */
	private Long mainPostId;

	/**
	 * 主岗位名称
	 */
	private String mainPostName;

	// TODO 副岗位
	/**
	 * 角色编码信息
	 */
	private Set<Long> roleIdSet;

	/**
	 * 权限信息
	 */
	private List<String> permissions;

}
