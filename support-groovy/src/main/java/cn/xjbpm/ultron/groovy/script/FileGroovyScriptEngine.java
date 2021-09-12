package cn.xjbpm.ultron.groovy.script;

import cn.xjbpm.ultron.groovy.properties.GroovyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/31
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "groovy.script.dataBaseScriptSource", havingValue = "false")
public class FileGroovyScriptEngine extends GroovyScriptEngine {

	private static final String LANGUAGE = "groovy";

	private static final String FILE_SUFFIX = ".groovy";

	public FileGroovyScriptEngine(DefaultListableBeanFactory defaultListableBeanFactory,
			GroovyProperties groovyProperties) {
		super(defaultListableBeanFactory, groovyProperties);
	}

	@Override
	protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader) {
		return new ResourceScriptSource(resourceLoader.getResource(String.format("file:%s", scriptSourceLocator)));
	}

	/**
	 * 扫描脚本
	 */
	@Override
	public void scanScript() {
		String groovyDirectoryPath = GroovyScriptEngine.class.getClassLoader().getResource(LANGUAGE).getPath();
		File groovyDirectory = new File(groovyDirectoryPath);
		if (groovyDirectory.isDirectory()) {
			File[] groovyScript = groovyDirectory.listFiles((dir, name) -> name.endsWith(FILE_SUFFIX));
			scanNewScriptAndAutoRegister(groovyScript);
			scanDeleteScriptAndAutoRemove(groovyScript);
		}
	}

	/**
	 * 扫描已删除的脚本并从容器中自动删除
	 * <p>
	 * 通过遍历已注册脚本反向检查文件是否存在，若不存在则删除已注册脚本
	 * @param scriptFiles
	 */
	private void scanDeleteScriptAndAutoRemove(File[] scriptFiles) {
		ALL_SCRIPT_BEANS.forEach((fileName, beanName) -> {
			List<String> collect = Arrays.stream(scriptFiles).map(File::getName).collect(Collectors.toList());
			if (!collect.contains(fileName)) {
				try {
					remove(beanName);
					ALL_SCRIPT_BEANS.remove(fileName);
					log.info("脚本{}已被移除", fileName);
				}
				catch (Exception e) {
					log.info("扫描到移除脚本{}，但是移除失败", fileName, e);
				}
			}
		});
	}

	/**
	 * 检查新脚本并自动注册到容器里
	 * @param scriptFiles
	 */
	private void scanNewScriptAndAutoRegister(File[] scriptFiles) {
		for (File script : scriptFiles) {
			String scriptName = script.getName();
			if (!ALL_SCRIPT_BEANS.containsKey(scriptName)) {
				try {
					String simpleBeanName = register(script);
					ALL_SCRIPT_BEANS.put(scriptName, simpleBeanName);
					log.info("脚本{}注册完成,文件路径:{}", script.getName(), script.getAbsolutePath());
				}
				catch (Exception e) {
					log.info("扫描到新的脚本 {}，但是注册失败", script.getName(), e);
				}
			}
		}
	}

}
