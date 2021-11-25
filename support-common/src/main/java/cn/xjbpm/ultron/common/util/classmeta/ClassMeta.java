package cn.xjbpm.ultron.common.util.classmeta;

import cn.xjbpm.ultron.common.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com 参考 nutz
 */
public class ClassMeta {

	public String type;

	public Map<String, List<String>> paramNames = new HashMap<String, List<String>>();

	public Map<String, Integer> methodLines = new HashMap<String, Integer>();

	@Override
	public String toString() {
		return JsonUtil.obj2Json(this);
	}

}
