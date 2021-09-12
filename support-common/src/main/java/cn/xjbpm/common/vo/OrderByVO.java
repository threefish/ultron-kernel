package cn.xjbpm.common.vo;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
public class OrderByVO {

	String field;

	Type type;

	@Getter
	public enum Type {

		ascend(Sort.Direction.ASC),

		descend(Sort.Direction.DESC);

		Sort.Direction sort;

		Type(Sort.Direction sort) {
			this.sort = sort;
		}

	}

}
