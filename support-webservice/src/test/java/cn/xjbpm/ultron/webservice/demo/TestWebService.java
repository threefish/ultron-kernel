package cn.xjbpm.ultron.webservice.demo;

import cn.xjbpm.common.vo.JsonResultVO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface TestWebService {

	/**
	 * @param name
	 * @return
	 */
	@WebMethod
	JsonResultVO orderStatusSyn(@WebParam String name);

}
