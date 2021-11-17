package cn.xjbpm.ultron.web.event;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.xjbpm.common.event.AutoMenuAuthDTO;
import cn.xjbpm.common.event.AutoRegisterMenuEvent;
import cn.xjbpm.common.util.BeanContextUtil;
import cn.xjbpm.common.util.JsonUtil;
import cn.xjbpm.common.util.spel.SpELUtil;
import cn.xjbpm.ultron.web.annotation.GenerateMenu;
import cn.xjbpm.ultron.web.annotation.GenerateMenuGroup;
import cn.xjbpm.ultron.web.annotation.SecurityPermissions;
import cn.xjbpm.ultron.web.util.GenerateMenuUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @author 黄川 huchuc@vip.qq.com 菜单自动发现注册服务
 */
@ConditionalOnClass(RequestMappingHandlerMapping.class)
@Component
@RequiredArgsConstructor
@Slf4j
public class AutoRegisterMenuAuthApplicationReadyEvent {

	private static volatile HashMap<String, AutoMenuAuthDTO> hashMap = MapUtil.newHashMap();

	private static volatile Set<Class> hashSet = new HashSet<>();

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	@EventListener({ ApplicationReadyEvent.class })
	public void onApplicationReady() {
		// 推送自动注册菜单事件
		BeanContextUtil.publishAsyncEvent(new AutoRegisterMenuEvent(this, this.scanRequestMapping()));
	}

	/**
	 * 扫描菜单并生成需要注册的菜单
	 * @return
	 */
	public List<AutoMenuAuthDTO> scanRequestMapping() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
		Iterator iterator = handlerMethods.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry = (Map.Entry) iterator
					.next();
			HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();
			RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
			Collection<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
			Object[] objects = patterns.toArray();
			if (objects.length > 0) {
				String path = String.valueOf(objects[0]);
				final GenerateMenuGroup generateMenuGroup = handlerMethod.getBeanType()
						.getAnnotation(GenerateMenuGroup.class);
				final Map<String, Object> context = GenerateMenuUtil.buildContext(generateMenuGroup);
				this.pushGroupMenus(generateMenuGroup, context, handlerMethod.getBeanType());
				final GenerateMenu methodGenerateMenu = handlerMethod.getMethod().getAnnotation(GenerateMenu.class);
				final SecurityPermissions securityPermissions = handlerMethod.getMethod()
						.getAnnotation(SecurityPermissions.class);
				if (Objects.nonNull(methodGenerateMenu)) {
					AutoMenuAuthDTO menuVO = buildAutoMenuAuth(methodGenerateMenu, securityPermissions, context);
					menuVO.setRequestMappingPath(path);
					Assert.hasText(menuVO.getCustomizeId(),
							String.format("菜单 CustomizeId 不能为空：%s", handlerMethod.getShortLogMessage()));
					Assert.hasText(menuVO.getName(), String.format("菜单名称不能为空：%s", handlerMethod.getShortLogMessage()));
					Assert.isTrue(!hashMap.containsKey(menuVO.getCustomizeId()),
							String.format("[%s]菜单自定义ID不能出现重复：%s %s", menuVO.getName(), menuVO.getCustomizeId(),
									handlerMethod.getShortLogMessage()));
					hashMap.put(menuVO.getCustomizeId(), menuVO);
				}
			}
		}
		List<AutoMenuAuthDTO> menus = new ArrayList<>();
		hashMap.forEach((s, menuVO) -> menus.add(menuVO));
		return menus;
	}

	private synchronized void pushGroupMenus(GenerateMenuGroup annotation, Map<String, Object> context,
			Class<?> beanClass) {
		if (!hashSet.contains(beanClass) && Objects.nonNull(annotation)) {
			hashSet.add(beanClass);
			final GenerateMenu[] menus = annotation.menus();
			for (GenerateMenu menu : menus) {
				final AutoMenuAuthDTO menuVO = buildAutoMenuAuth(menu, null, context);
				menuVO.setRequestMappingPath(getClassRequestMappingPath(beanClass));
				Assert.hasText(menuVO.getCustomizeId(), String.format("类上绑定的菜单 -> CustomizeId 不能为空：%s", beanClass));
				Assert.hasText(menuVO.getName(), String.format("类上绑定的菜单 -> 菜单名称不能为空：%s", beanClass));
				Assert.isTrue(!hashMap.containsKey(menuVO.getCustomizeId()),
						String.format("[%s]菜单自定义ID不能出现重复：%s %s", menuVO.getName(), menuVO.getCustomizeId(), beanClass));
				hashMap.put(menuVO.getCustomizeId(), menuVO);
			}
		}
	}

	private AutoMenuAuthDTO buildAutoMenuAuth(GenerateMenu methodGenerateMenu, SecurityPermissions securityPermissions,
			Map<String, Object> context) {
		AutoMenuAuthDTO menuVO = new AutoMenuAuthDTO();
		menuVO.setName(getMenuName(methodGenerateMenu, context));
		menuVO.setCustomizeId(getCustomizeId(methodGenerateMenu, context));
		menuVO.setParentCustomizeId(getParentCustomizeId(methodGenerateMenu, context));
		menuVO.setPermissions(getPermissions(methodGenerateMenu, securityPermissions, context));
		menuVO.setType(methodGenerateMenu.type());
		menuVO.setIcon(methodGenerateMenu.icon());
		return menuVO;
	}

	/**
	 * 获取菜单唯一ID
	 * @param methodGenerateMenu
	 * @return
	 */
	private String getCustomizeId(GenerateMenu methodGenerateMenu, Map<String, Object> context) {
		String expression = StrUtil.isNotBlank(methodGenerateMenu.customizeId()) ? methodGenerateMenu.customizeId()
				: "";
		final String value = SpELUtil.parseValueToString(context, expression);
		if (log.isTraceEnabled()) {
			log.trace("获取菜单唯一ID -> 表达式:'{}' 计算后：'{}' 上下文:'{}'", expression, value, JsonUtil.obj2Json(context));
		}
		return value;
	}

	/**
	 * 获取菜单唯一ID
	 * @param methodGenerateMenu
	 * @return
	 */
	private String getParentCustomizeId(GenerateMenu methodGenerateMenu, Map<String, Object> context) {
		String expression = StrUtil.isNotBlank(methodGenerateMenu.parentCustomizeId())
				? methodGenerateMenu.parentCustomizeId() : "";
		final String value = SpELUtil.parseValueToString(context, expression);
		if (log.isTraceEnabled()) {
			log.trace("获取菜单唯一ID -> 表达式:'{}' 计算后：'{}' 上下文:'{}'", expression, value, JsonUtil.obj2Json(context));
		}
		return value;
	}

	/**
	 * 获取权限字符串
	 * @param methodGenerateMenu
	 * @return
	 */
	private String getPermissions(GenerateMenu methodGenerateMenu, SecurityPermissions securityPermissions,
			Map<String, Object> context) {
		String expression = StrUtil.isNotBlank(methodGenerateMenu.permissions()) ? methodGenerateMenu.permissions()
				: "";
		if (StrUtil.isBlank(expression) && Objects.nonNull(securityPermissions)) {
			expression = StrUtil.isNotBlank(securityPermissions.value()[0]) ? securityPermissions.value()[0] : "";
		}
		final String value = SpELUtil.parseValueToString(context, expression);
		if (log.isTraceEnabled()) {
			log.trace("获取权限字符串 -> 表达式:'{}' 计算后：'{}' 上下文:'{}'", expression, value, JsonUtil.obj2Json(context));
		}
		return value;
	}

	/**
	 * 获取菜单名称
	 * @param methodGenerateMenu
	 * @return
	 */
	private String getMenuName(GenerateMenu methodGenerateMenu, Map<String, Object> context) {
		String expression = StrUtil.isNotBlank(methodGenerateMenu.name()) ? methodGenerateMenu.name() : "";
		final String value = SpELUtil.parseValueToString(context, expression);
		if (log.isTraceEnabled()) {
			log.trace("获取菜单名称 -> 表达式:'{}' 计算后：'{}' 上下文:'{}'", expression, value, JsonUtil.obj2Json(context));
		}
		return value;
	}

	/**
	 * 获取类上菜单地址，如果不存在 RequestMapping 注解就取方法类名来做菜单组path
	 * @param klass
	 * @return
	 */
	private String getClassRequestMappingPath(Class<?> klass) {
		RequestMapping requestMapping = klass.getAnnotation(RequestMapping.class);
		if (Objects.nonNull(requestMapping)) {
			String[] value = requestMapping.value();
			String[] path = requestMapping.path();
			if (Objects.isNull(value) && Objects.nonNull(path)) {
				value = path;
			}
			return value[0];
		}
		else {
			return StrUtil.lowerFirst(klass.getName());
		}
	}

}
