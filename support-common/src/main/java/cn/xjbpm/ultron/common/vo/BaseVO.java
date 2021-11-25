package cn.xjbpm.ultron.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/9
 */
@Data
public class BaseVO<ID extends Serializable> {

	@Schema(description = "主键", example = "1")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	protected ID id;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人", example = "admin")
	protected String createdBy;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间", example = "2021-05-09 18:30:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime createDate;

	/**
	 * 最后修改时间
	 */
	@Schema(description = "修改人", example = "admin")
	protected String lastModifiedBy;

	/**
	 * 更新时间
	 */
	@Schema(description = "最后修改时间", example = "2021-05-09 18:30:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime lastModifiedDate;

}
