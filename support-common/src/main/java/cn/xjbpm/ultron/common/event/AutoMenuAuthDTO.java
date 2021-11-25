package cn.xjbpm.ultron.common.event;

import cn.xjbpm.ultron.common.enums.MenuType;
import lombok.Data;

/**
 * @author 黄川 huchuc@vip.qq.com 菜单自动扫描注册
 */
@Data
public class AutoMenuAuthDTO {

	/**
	 * 菜单类型
	 */
	MenuType type;

	/**
	 * 菜单名称
	 */
	String name;

	/**
	 * 自定义 ID （根据子父ID计算上下级）
	 */
	String customizeId;

	/**
	 * 自定义 父ID （根据子父ID计算上下级）
	 */
	String parentCustomizeId;

	/**
	 * 请求地址
	 */
	String requestMappingPath;

	/**
	 * 权限标识
	 */
	String permissions;

	/**
	 * 图标
	 */
	String icon;

}
