package cn.xjbpm.ultron.common.tree;

import java.util.Collection;
import java.util.List;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public interface DataTree<T> {

	/**
	 * 得到Id
	 * @return {@link String}
	 */
	String getId();

	/**
	 * 得到父Id
	 * @return {@link String}
	 */
	String getParentId();

	/**
	 * 取得子节点
	 * @return {@link List<T>}
	 */
	Collection<T> getChildren();

	/**
	 * 设置子节点
	 * @param childList 孩子列表
	 */
	void setChildren(Collection<T> childList);

}
