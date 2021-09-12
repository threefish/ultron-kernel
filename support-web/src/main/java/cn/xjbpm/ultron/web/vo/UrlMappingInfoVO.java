package cn.xjbpm.ultron.web.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/29
 */
@Data
public class UrlMappingInfoVO {

	/**
	 * 请求方法
	 */
	private Set<RequestMethod> method;

	/**
	 * 控制器方法上的路由集
	 */
	private Set<String> patterns;

	/**
	 * bean名称
	 */
	private Object bean;

	/**
	 * 控制器方法描述
	 */
	private String description;

}
