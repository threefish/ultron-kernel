package cn.xjbpm.ultron.jpa.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author 黄川 huchuc@vip.qq.com
 * @MappedSuperclass 标识得类不能再有@Entity或@Table注解 但是可以使用@Id 和@Column注解
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	/**
	 * 创建人
	 */
	@CreatedBy
	protected String createdBy;

	/**
	 * 创建时间
	 */
	@CreatedDate
	protected LocalDateTime createDate;

	/**
	 * 最后修改人
	 */
	@LastModifiedBy
	protected String lastModifiedBy;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	protected LocalDateTime lastModifiedDate;

}
