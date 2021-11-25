package cn.xjbpm.ultron.webservice.demo;

import cn.xjbpm.ultron.common.vo.JsonResultVO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "testWebService", targetNamespace = "demo.webservice.ultron.xjbpm.cn")
public interface TestWebService {

	/**
	 * @param name
	 * @return
	 */
	@WebMethod
	JsonResultVO orderStatusSyn(@WebParam String name);

}
