package cn.xjbpm.ultron.jpa.entity;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com 主键字段数据库自增 id bigint not null auto_increment
 */
@Data
@MappedSuperclass
public class BaseIncrementIdEntity<ID extends Serializable> extends BaseEntity implements Persistable<ID> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected ID id;

	@Transient
	@Override
	public boolean isNew() {
		return null == getId();
	}

}
