package cn.xjbpm.ultron.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.xjbpm.common.exception.HttpStatusExceptionEnum;
import cn.xjbpm.common.vo.JsonResultVO;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class UltronBasicErrorController extends BasicErrorController {

	public UltronBasicErrorController(ServerProperties serverProperties) {
		super(new DefaultErrorAttributes(), serverProperties.getError());
	}

	/**
	 * 覆盖默认的JSON响应
	 */
	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		final Map<String, Object> map = BeanUtil
				.beanToMap(JsonResultVO.failed(HttpStatusExceptionEnum.RESOURCE_NOT_FOUND, request.getRequestURI()));
		return new ResponseEntity<>(map, getStatus(request));
	}

}
