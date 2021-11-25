package cn.xjbpm.ultron.common.vo;

import cn.xjbpm.ultron.common.tree.DataTree;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Data
@NoArgsConstructor
public class TreeNodeVO implements DataTree {

	/**
	 * 被树的 (default)ExpandedKeys / (default)CheckedKeys / (default)SelectedKeys
	 * 属性所用。注意：整个树范围内的所有节点的 key 值不能重复！
	 */
	String key;

	/**
	 * 父节点key
	 */
	String parentKey;

	/**
	 * 标题
	 */
	String title;

	/**
	 * icon 自定义图标。可接收组件，props 为当前节点 props
	 */
	String icon;

	/**
	 * 禁掉 checkable
	 */
	Boolean checkable;

	/**
	 * 禁掉 checkbox
	 */
	Boolean disableCheckbox;

	/**
	 * 禁掉响应
	 */
	Boolean disabled;

	/**
	 * 设置节点是否可被选中
	 */
	Boolean selectable;

	/**
	 * 设置为叶子节点 (设置了 loadData 时有效)。为 false 时会强制将其作为父节点
	 */
	Boolean isLeaf;

	Set<TreeNodeVO> children;

	public TreeNodeVO(String key, String parentKey, String title) {
		this.key = key;
		this.parentKey = parentKey;
		this.title = title;
	}

	@Override
	public String getId() {
		return key;
	}

	@Override
	public String getParentId() {
		return parentKey;
	}

	@Override
	public Collection getChildren() {
		return children;
	}

	@Override
	public void setChildren(Collection childList) {
		children = new HashSet<>(childList);
	}

}
