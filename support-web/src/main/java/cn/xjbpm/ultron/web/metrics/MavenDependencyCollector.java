package cn.xjbpm.ultron.web.metrics;

import cn.xjbpm.common.util.maven.MavenDependencyUtil;
import cn.xjbpm.common.util.maven.MavenDependencyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.TreeSet;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20
 */
@Slf4j
@RequestMapping("/mvc/api/v1/maven")
@RestController
@RequiredArgsConstructor
public class MavenDependencyCollector {

	private static SoftReference<TreeSet<MavenDependencyVO>> mavenInfoSoftReference;

	@GetMapping("/list")
	public TreeSet<MavenDependencyVO> list() {
		TreeSet<MavenDependencyVO> mavenDependencys = Objects.isNull(mavenInfoSoftReference) ? null
				: mavenInfoSoftReference.get();
		if (Objects.isNull(mavenDependencys)) {
			try {
				mavenDependencys = MavenDependencyUtil.getMavenInfo();
			}
			catch (IOException e) {
				log.warn("getMavenInfo info failed.", e);
				return null;
			}
		}
		mavenInfoSoftReference = new SoftReference(mavenDependencys);
		return mavenDependencys;
	}

}
