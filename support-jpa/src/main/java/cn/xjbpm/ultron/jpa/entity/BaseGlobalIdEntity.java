package cn.xjbpm.ultron.jpa.entity;

import cn.xjbpm.ultron.jpa.identifier.GlobalIdentifierGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com 程序控制生成全局ID
 */
@Data
@MappedSuperclass
public class BaseGlobalIdEntity<ID extends Serializable> extends BaseEntity implements Persistable<ID> {

	@Id
	@GeneratedValue(generator = "global")
	@GenericGenerator(name = "global", strategy = GlobalIdentifierGenerator.CLASS_NAME)
	@Column(name = "id", nullable = false)
	protected ID id;

	@Transient
	@Override
	public boolean isNew() {
		return null == getId();
	}

}
