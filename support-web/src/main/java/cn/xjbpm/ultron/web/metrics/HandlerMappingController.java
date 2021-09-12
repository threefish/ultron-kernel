package cn.xjbpm.ultron.web.metrics;

import cn.xjbpm.ultron.web.vo.UrlMappingInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/5/29 当前系统的http请求映射列表
 */
@RequestMapping("/mvc/api/v1/RequestMapping")
@RestController
@RequiredArgsConstructor
public class HandlerMappingController {

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	@GetMapping("/list")
	public List<UrlMappingInfoVO> list() {
		List<UrlMappingInfoVO> result = new ArrayList<>();
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		map.forEach((info, method) -> {
			UrlMappingInfoVO urlMappingInfo = new UrlMappingInfoVO();
			PatternsRequestCondition patternsRequestCondition = info.getPatternsCondition();
			urlMappingInfo.setMethod(info.getMethodsCondition().getMethods());
			urlMappingInfo.setPatterns(patternsRequestCondition.getPatterns());
			urlMappingInfo.setBean(method.getBean());
			urlMappingInfo.setDescription(method.toString());
			result.add(urlMappingInfo);
		});
		return result;
	}

}
