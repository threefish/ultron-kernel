package cn.xjbpm.ultron.web.util;

import cn.xjbpm.ultron.web.annotation.GenerateMenuGroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/3
 */
public class GenerateMenuUtil {

	public static Map<String, Object> buildContext(GenerateMenuGroup generateMenuGroup) {
		if (Objects.isNull(generateMenuGroup)) {
			return Collections.emptyMap();
		}
		Map<String, Object> ctx = new HashMap<>(10);
		ctx.put("args", generateMenuGroup.args());
		return ctx;
	}

}
