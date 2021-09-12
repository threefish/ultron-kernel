package cn.xjbpm.common.util.maven;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20 TODO 打包后有问题需要重构下
 */
@Slf4j
@UtilityClass
public class MavenDependencyUtil {

	public static TreeSet<MavenDependencyVO> getMavenInfo() throws IOException {
		Enumeration<URL> resources = MavenDependencyUtil.class.getClassLoader().getResources("META-INF/maven");
		TreeSet mavenDependencies = new TreeSet();
		while (resources.hasMoreElements()) {
			try {
				URL url = resources.nextElement();
				if (log.isTraceEnabled()) {
					log.trace("Attempt to load maven properties from: '{}'", url.toString());
				}
				Collection<String> pomPropertySet = MavenDependencyUtil.listPomProperties(url);
				mavenDependencies.addAll(MavenDependencyUtil.buildMavenDependencyVO(pomPropertySet));
			}
			catch (Exception e) {
				if (log.isTraceEnabled()) {
					log.trace("Load maven properties failed.", e);
				}
			}
		}
		return mavenDependencies;
	}

	public static Set<MavenDependencyVO> buildMavenDependencyVO(Collection<String> pomPropertySet) throws IOException {
		Set<MavenDependencyVO> mavenDependencies = new HashSet(pomPropertySet.size());
		Iterator iterator = pomPropertySet.iterator();
		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			InputStream is = MavenDependencyUtil.class.getResourceAsStream(s);
			Properties properties = new Properties();
			properties.load(is);
			String version = properties.getProperty("version");
			String groupId = properties.getProperty("groupId");
			String artifactId = properties.getProperty("artifactId");
			mavenDependencies.add(new MavenDependencyVO(groupId, artifactId, version));
		}
		return mavenDependencies;
	}

	public static Collection<String> listPomProperties(URL mavenPath) throws Exception {
		Set<String> pomProperties = new HashSet();
		FileSystem fileSystem = null;
		try {
			String[] pathArray = mavenPath.getPath().split("!");
			boolean isPathInJarInnerLib = pathArray.length == 3;
			URI uri = mavenPath.toURI();
			Path myPath;
			if ("jar".equals(uri.getScheme())) {
				try {
					fileSystem = FileSystems.getFileSystem(uri);
				}
				catch (FileSystemNotFoundException e) {
					fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
				}
				myPath = fileSystem.getPath(pathArray[1]);
				if (isPathInJarInnerLib) {
					return MavenDependencyUtil.listPomPropertiesFromLibJar(myPath);
				}
			}
			else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 4, new FileVisitOption[0]);
			Iterator it = walk.iterator();
			while (it.hasNext()) {
				Path path = (Path) it.next();
				if (path.toString().endsWith("pom.properties")) {
					pomProperties.add(path.toString());
				}
			}
			return pomProperties;
		}
		catch (Exception e) {
			if (log.isTraceEnabled()) {
				log.trace("Collect maven info of {} failed.", mavenPath, e);
			}
			return pomProperties;
		}
		finally {
			if (fileSystem != null) {
				fileSystem.close();
			}
		}
	}

	public static Collection<String> listPomPropertiesFromLibJar(Path zipPath) {
		try {
			InputStream in = Files.newInputStream(zipPath);
			File tempFile = File.createTempFile(zipPath.getFileName().toString(), "jar");
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(in, out);
			Collection<String> pomProperties = MavenDependencyUtil
					.listPomProperties(new URL(String.format("jar:file:%s!/META-INF/maven", tempFile.getPath())));
			tempFile.delete();
			return pomProperties;
		}
		catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
