/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/9/13
 */
@Data
public abstract class BaseAuditEntity<ID extends Serializable> implements Serializable, Persistable<ID> {

	/**
	 * 创建人
	 */
	@TableField(value = "created_by", fill = FieldFill.INSERT)
	protected String createdBy;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_date", fill = FieldFill.INSERT)
	protected LocalDateTime createDate;

	/**
	 * 修改人
	 */
	@TableField(value = "last_modified_by", fill = FieldFill.UPDATE)
	protected String lastModifiedBy;

	/**
	 * 修改时间
	 */
	@TableField(value = "last_modified_date", fill = FieldFill.UPDATE)
	protected LocalDateTime lastModifiedDate;

}
