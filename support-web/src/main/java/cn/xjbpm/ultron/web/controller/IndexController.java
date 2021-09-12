package cn.xjbpm.ultron.web.controller;

import cn.xjbpm.common.vo.JsonResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/3
 */
@RestController
public class IndexController {

	@GetMapping("/")
	public JsonResultVO index() {
		return JsonResultVO.sucess("应用运行中");
	}

	@GetMapping("/favicon.icon")
	public JsonResultVO favicon() {
		return index();
	}

}
