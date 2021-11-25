package cn.xjbpm.ultron.common.vo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@Schema(description = "分页数据查询基础对象")
public class PageReqVO implements Serializable {

	/**
	 * 起始页
	 **/
	@Schema(description = "当前页码", example = "1")
	private Integer current;

	/**
	 * 每页查询数量
	 **/
	@Schema(description = "每页查询数量", example = "10")
	private Integer pageSize;

	@Schema(description = "排序信息", example = "{}")
	private List<OrderByVO> orders;

	public PageReqVO() {
		this.current = 1;
		this.pageSize = 10;
	}

	public static PageRequest of(int page, int size, Sort.Direction direction, String... properties) {
		page = Math.max(page, 1);
		return PageRequest.of(page - 1, size, direction, properties);
	}

	public PageRequest toPageRequest() {
		List<Sort.Order> list = new ArrayList();
		if (CollUtil.isNotEmpty(orders)) {
			orders.forEach(order -> list.add(new Sort.Order(order.getType().getSort(), order.getField())));
		}
		return toPageRequest(Sort.by(list));
	}

	public PageRequest toPageRequest(Sort sort) {
		this.current = Math.max(this.current, 1);
		return PageRequest.of(this.current - 1, this.pageSize, sort);
	}

}
