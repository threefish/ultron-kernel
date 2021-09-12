package cn.xjbpm.ultron.webservice.demo;

import cn.xjbpm.common.vo.JsonResultVO;
import cn.xjbpm.ultron.webservice.annotation.WebServiceProvider;
import org.springframework.stereotype.Service;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Service
@WebServiceProvider(serviceName = "testService")
public class TestWebServiceImpl implements TestWebService {

	@Override
	public JsonResultVO orderStatusSyn(String name) {
		return JsonResultVO.sucess("好的");
	}

}
