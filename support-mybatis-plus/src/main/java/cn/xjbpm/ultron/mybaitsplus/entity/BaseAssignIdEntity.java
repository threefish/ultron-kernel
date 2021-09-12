/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/9/29 指定全局ID生成服务产生的主键ID
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseAssignIdEntity<ID extends Serializable> extends BaseAuditEntity implements Serializable {

	/**
	 * 推荐数据库字段长度20 全局ID服务产生的id，数据库一定要设置id为主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	protected ID id;

	@Transient
	@Override
	public boolean isNew() {
		return Objects.isNull(getId());
	}

}
