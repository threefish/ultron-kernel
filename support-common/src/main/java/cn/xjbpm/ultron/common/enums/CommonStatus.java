package cn.xjbpm.ultron.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/9
 */
@AllArgsConstructor
@Getter
public enum CommonStatus {

	/**
	 * 禁用的
	 */
	DISABLED("禁用的"),
	/**
	 * 启用的
	 */
	ENABLED("启用的");

	String description;

}
