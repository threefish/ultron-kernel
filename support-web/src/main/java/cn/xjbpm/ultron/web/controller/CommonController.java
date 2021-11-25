package cn.xjbpm.ultron.web.controller;

import cn.xjbpm.ultron.common.enums.MenuType;
import cn.xjbpm.ultron.common.vo.BaseVO;
import cn.xjbpm.ultron.common.vo.PageReqVO;
import cn.xjbpm.ultron.common.vo.PageRespVO;
import cn.xjbpm.ultron.redis.cache.resolver.CommonCacheResolver;
import cn.xjbpm.ultron.web.annotation.GenerateMenu;
import cn.xjbpm.ultron.web.annotation.NoRepeatSubmit;
import cn.xjbpm.ultron.web.annotation.SecurityPermissions;
import cn.xjbpm.ultron.web.service.CommonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com 继承当前类，记得打 @CommonControllerCache 注解
 */
public class CommonController<Service extends CommonService<ID, VO, PageVO>, ID extends Serializable, VO extends BaseVO, PageVO extends PageReqVO> {

	@Autowired
	protected Service service;

	@ApiOperation("保存对象")
	@PostMapping("/save")
	@GenerateMenu(name = "保存对象", type = MenuType.RESOURCE, customizeId = "#{args[0]}_save",
			parentCustomizeId = "#{args[0]}_manage")
	@SecurityPermissions("#{args[0]}_save")
	@NoRepeatSubmit
	@CacheEvict(key = "#vo.id", condition = "#vo.id != null", cacheResolver = CommonCacheResolver.BEAN_NAME)
	public boolean save(@RequestBody VO vo) {
		return service.save(vo);
	}

	@ApiOperation("获取对象信息")
	@PostMapping("/get")
	@GenerateMenu(name = "获取对象信息", type = MenuType.RESOURCE, customizeId = "#{args[0]}_get",
			parentCustomizeId = "#{args[0]}_manage")
	@SecurityPermissions("#{args[0]}_get")
	@Cacheable(key = "#id", cacheResolver = CommonCacheResolver.BEAN_NAME)
	public VO get(@NotNull(message = "主键ID不允许为空") @RequestParam ID id) {
		return service.findById(id);
	}

	@ApiOperation("删除对象信息")
	@PostMapping("/deleteById")
	@GenerateMenu(name = "删除对象信息", type = MenuType.RESOURCE, customizeId = "#{args[0]}_delete",
			parentCustomizeId = "#{args[0]}_manage")
	@SecurityPermissions("#{args[0]}_delete")
	@CacheEvict(key = "#id", cacheResolver = CommonCacheResolver.BEAN_NAME)
	public boolean deleteById(@NotNull(message = "主键ID不允许为空") @RequestParam ID id) {
		return service.deleteById(id);
	}

	@ApiOperation("分页查询对象信息")
	@PostMapping("/listByPageQuery")
	@GenerateMenu(name = "分页查询对象信息", type = MenuType.RESOURCE, customizeId = "#{args[0]}_list_by_page_query",
			parentCustomizeId = "#{args[0]}_manage")
	@SecurityPermissions("#{args[0]}_list_by_page_query")
	public PageRespVO<Iterable<VO>> listByPageQuery(@RequestBody PageVO pageVO) {
		return service.listByPageQuery(pageVO);
	}

}
