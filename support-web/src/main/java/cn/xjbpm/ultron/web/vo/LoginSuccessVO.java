package cn.xjbpm.ultron.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessVO {

	/**
	 * 访问token
	 */
	String accessToken;

	/**
	 * token过期时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	Date accessTokenExpireTime;

	/**
	 * 刷新token
	 */
	String refreshToken;

	/**
	 * 刷新token的过期时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	Date refreshTokenExpireTime;

}
