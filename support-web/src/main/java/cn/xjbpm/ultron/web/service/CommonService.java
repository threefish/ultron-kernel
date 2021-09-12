package cn.xjbpm.ultron.web.service;

import cn.xjbpm.common.vo.BaseVO;
import cn.xjbpm.common.vo.PageReqVO;
import cn.xjbpm.common.vo.PageRespVO;

import java.io.Serializable;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface CommonService<ID extends Serializable, VO extends BaseVO, PageVO extends PageReqVO> {

	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	boolean save(VO vo);

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	VO findById(ID id);

	/**
	 * 分页查询
	 * @param pageVO
	 * @return
	 */
	PageRespVO<Iterable<VO>> listByPageQuery(PageVO pageVO);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	boolean deleteById(ID id);

}
