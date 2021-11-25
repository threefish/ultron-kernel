/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class TenantContextHolder {

	/**
	 * 支持父子线程之间的数据传递
	 */
	private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	/**
	 * 获取租户ID
	 * @return String
	 */
	public Optional<String> getTenantId() {
		return Optional.ofNullable(THREAD_LOCAL_TENANT.get());
	}

	/**
	 * 设置租户
	 * @param tenantId 租户ID
	 */
	public void setTenantId(String tenantId) {
		THREAD_LOCAL_TENANT.set(tenantId);
	}

	/**
	 * 清除租户ID
	 */
	public void clear() {
		THREAD_LOCAL_TENANT.remove();
	}

}
