package cn.xjbpm.common.convert;

/**
 * @author 黄川 huchuc@vip.qq.com
 */

import java.util.Collection;

/**
 * @author 黄川 huchuc@vip.qq.com 2021/2/16
 * <p>
 * 中文文档：http://www.kailing.pub/MapStruct1.3/index.html
 * 官网英文原版文档：https://mapstruct.org/documentation/1.1/reference/html/index.html
 */
public interface BaseConvert<VO, DO> {

	/**
	 * vo转do
	 * @param vo
	 * @return
	 */
	DO voToDo(VO vo);

	/**
	 * dto转vo
	 * @param entity
	 * @return
	 */
	VO doToVo(DO entity);

	/**
	 * vo集合转do集合
	 * @param vos
	 * @return
	 */
	Collection<DO> voToDo(Iterable<VO> vos);

	/**
	 * do集合转vo集合
	 * @param entityList
	 * @return
	 */
	Collection<VO> doToVo(Iterable<DO> entityList);

}
