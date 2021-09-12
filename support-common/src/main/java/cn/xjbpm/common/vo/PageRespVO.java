package cn.xjbpm.common.vo;

import cn.xjbpm.common.constant.JsonResultConstant;
import cn.xjbpm.common.convert.BaseConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@Schema(description = "统一数据返回分页列表结果集")
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageRespVO<T> extends JsonResultVO implements Serializable {

	/***
	 * 返回结果数
	 */
	@Schema(description = "返回结果数", example = "0")
	private Long total;

	/***
	 * 返回结果数
	 */
	@Schema(description = "总分页数", example = "0")
	private Long totalPages;

	/**
	 * 返回数据
	 */
	@Schema(description = "返回数据", example = "[]")
	private Iterable<T> data;

	public static <T> PageRespVO of(Iterable<T> data, long totalElements, long totalPages) {
		PageRespVO pageRespVO = new PageRespVO(totalElements, totalPages, data);
		pageRespVO.setCode(JsonResultConstant.SUCCESS_CODE);
		pageRespVO.setMessage(JsonResultConstant.SUCCESS_MSG);
		pageRespVO.setData(data);
		pageRespVO.setTotal(totalElements);
		pageRespVO.setTotalPages(totalPages);
		return pageRespVO;
	}

	public static PageRespVO of(BaseConvert transfer, Page<?> data) {
		return of(transfer.doToVo(data.getContent()), data.getTotalElements(), data.getTotalPages());
	}

}
