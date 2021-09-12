package cn.xjbpm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/9
 */
@AllArgsConstructor
@Getter
public enum MenuType {

	GROUP("菜单组"),

	MENU("菜单项"),

	RESOURCE("资源");

	String description;

}
