package cn.xjbpm.ultron.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Getter
@AllArgsConstructor
public enum OperateType {

	ADD("增加"),

	DELETE("删除"),

	UPDATE("编辑"),

	SELECT("查询"),

	SAVE_OR_UPDATE("新增或者编辑"),

	EXPORT("导出"),

	IMPORT("导入"),

	LOGIN("登录"),

	LOGOUT("登出");

	String description;

}
