package cn.xjbpm.common.util.maven;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20
 */
@Data
@AllArgsConstructor
public class MavenDependencyVO implements Comparable<MavenDependencyVO> {

	private String groupId;

	private String artifactId;

	private String version;

	@Override
	public int compareTo(MavenDependencyVO o) {
		return groupId.compareTo(o.groupId);
	}

}
