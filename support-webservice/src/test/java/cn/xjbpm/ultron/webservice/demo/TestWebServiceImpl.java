package cn.xjbpm.ultron.webservice.demo;

import cn.xjbpm.common.vo.JsonResultVO;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Service
@WebService(serviceName = "testWebService", targetNamespace = "demo.webservice.ultron.xjbpm.cn")
public class TestWebServiceImpl implements TestWebService {

	@Override
	public JsonResultVO orderStatusSyn(String name) {
		return JsonResultVO.sucess("好的");
	}

}
