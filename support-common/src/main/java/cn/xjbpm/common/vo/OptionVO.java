package cn.xjbpm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "下拉框对象")
public class OptionVO implements Serializable {

	@Schema(description = "名称", example = "管理员")
	private String lable;

	@Schema(description = "值", example = "1")
	private String value;

}
