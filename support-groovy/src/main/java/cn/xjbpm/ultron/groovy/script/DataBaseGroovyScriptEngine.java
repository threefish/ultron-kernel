package cn.xjbpm.ultron.groovy.script;

import cn.xjbpm.ultron.groovy.entity.ScriptCode;
import cn.xjbpm.ultron.groovy.properties.GroovyProperties;
import cn.xjbpm.ultron.groovy.repository.DataBaseScriptSourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scripting.ScriptSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/31
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "groovy.script.dataBaseScriptSource", havingValue = "true")
public class DataBaseGroovyScriptEngine extends GroovyScriptEngine {

	private final DataBaseScriptSourceRepository dataBaseScriptSourceRepository;

	public DataBaseGroovyScriptEngine(DefaultListableBeanFactory defaultListableBeanFactory,
			GroovyProperties groovyProperties, DataBaseScriptSourceRepository dataBaseScriptSourceRepository) {
		super(defaultListableBeanFactory, groovyProperties);
		this.dataBaseScriptSourceRepository = dataBaseScriptSourceRepository;
	}

	@Override
	protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader) {
		return new DataBaseScriptSource(scriptSourceLocator, dataBaseScriptSourceRepository);
	}

	/**
	 * 扫描脚本
	 */
	@Override
	public void scanScript() {
		Set<ScriptCode> scriptCodeSet = dataBaseScriptSourceRepository.findAllScriptCodeSet();
		scanNewScriptAndAutoRegister(scriptCodeSet);
		scanDeleteScriptAndAutoRemove(scriptCodeSet);
	}

	/**
	 * 扫描已删除的脚本并从容器中自动删除
	 * <p>
	 * 通过遍历已注册脚本反向检查文件是否存在，若不存在则删除已注册脚本
	 * @param scriptCodeSet
	 */
	private void scanDeleteScriptAndAutoRemove(Set<ScriptCode> scriptCodeSet) {
		ALL_SCRIPT_BEANS.forEach((fileName, beanName) -> {
			List<String> collect = scriptCodeSet.stream().map(ScriptCode::getCodeName).collect(Collectors.toList());
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
	 * @param scriptCodeSet
	 */
	private void scanNewScriptAndAutoRegister(Set<ScriptCode> scriptCodeSet) {
		for (ScriptCode script : scriptCodeSet) {
			String scriptName = script.getCodeName();
			if (!ALL_SCRIPT_BEANS.containsKey(scriptName)) {
				try {
					String simpleBeanName = register(script.getCodeName(), script.getCodeContent());
					ALL_SCRIPT_BEANS.put(scriptName, simpleBeanName);
					log.info("脚本{}注册完成", script.getCodeName());
				}
				catch (Exception e) {
					log.info("扫描到新的脚本 {}，但是注册失败", script.getCodeName(), e);
				}
			}
		}
	}

}
