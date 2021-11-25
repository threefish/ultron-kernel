package cn.xjbpm.ultron.common.util;

import cn.xjbpm.ultron.common.tree.DataTree;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/02/04
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 获取顶层节点
	 * @param topId
	 * @param entityList
	 * @param <T>
	 * @return
	 */
	public static <T extends DataTree<T>> List<T> getTreeList(String topId, List<T> entityList) {
		// 存储顶层的数据
		List<T> resultList = new ArrayList<>();
		Map<Object, T> treeMap = new HashMap<>(16);
		T itemTree;
		for (int i = 0; i < entityList.size() && !entityList.isEmpty(); i++) {
			itemTree = entityList.get(i);
			// 把所有的数据放到map当中，id为key
			treeMap.put(itemTree.getId(), itemTree);
			if (topId.equals(itemTree.getParentId()) || itemTree.getParentId() == null) {
				// 把顶层数据放到集合中
				resultList.add(itemTree);
			}
		}
		// 循环数据，把数据放到上一级的childen属性中
		for (int i = 0; i < entityList.size() && !entityList.isEmpty(); i++) {
			itemTree = entityList.get(i);
			// 在map集合中寻找父亲
			T data = treeMap.get(itemTree.getParentId());
			if (data != null) {
				// 判断父亲有没有
				if (data.getChildren() == null) {
					data.setChildren(new ArrayList<>());
				}
				// 把子节点 放到父节点childList当中
				data.getChildren().add(itemTree);
				// 把放好的数据放回map当中
				treeMap.put(itemTree.getParentId(), data);
			}
		}
		return resultList;
	}

}
