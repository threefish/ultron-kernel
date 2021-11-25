package cn.xjbpm.ultron.common.vo;

import cn.xjbpm.ultron.common.component.query.annotation.QueryField;
import cn.xjbpm.ultron.common.component.query.annotation.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/27
 */
@Data
@Schema(description = "下拉框查询对象")
public class SearchOptionReqVO implements Serializable {

	@Schema(description = "名称", example = "管理员")
	@QueryField(type = QueryType.LIKE)
	private String name;

}
