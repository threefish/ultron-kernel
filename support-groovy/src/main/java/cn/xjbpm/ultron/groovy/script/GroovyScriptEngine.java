package cn.xjbpm.ultron.groovy.script;

import cn.hutool.core.thread.ThreadUtil;
import cn.xjbpm.ultron.groovy.constant.GroovyScriptCons;
import cn.xjbpm.ultron.groovy.factory.GroovyFactory;
import cn.xjbpm.ultron.groovy.properties.GroovyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/30
 */
@Slf4j
public abstract class GroovyScriptEngine extends ScriptFactoryPostProcessor implements InitializingBean {

	protected static final Map<String, String> ALL_SCRIPT_BEANS = new ConcurrentHashMap<>();

	protected final DefaultListableBeanFactory defaultListableBeanFactory;

	protected final GroovyProperties groovyProperties;

	public GroovyScriptEngine(DefaultListableBeanFactory defaultListableBeanFactory,
			GroovyProperties groovyProperties) {
		this.defaultListableBeanFactory = defaultListableBeanFactory;
		this.groovyProperties = groovyProperties;
	}

	@Override
	protected ScriptSource convertToScriptSource(String beanName, String scriptSourceLocator,
			ResourceLoader resourceLoader) {
		return getScriptSource(beanName, scriptSourceLocator, resourceLoader);
	}

	/**
	 * getScriptSource
	 * @param beanName
	 * @param scriptSourceLocator
	 * @param resourceLoader
	 * @return
	 */
	protected abstract ScriptSource getScriptSource(String beanName, String scriptSourceLocator,
			ResourceLoader resourceLoader);

	/**
	 * 扫描脚本
	 */
	protected abstract void scanScript() throws IOException;

	/**
	 * 注册脚本
	 * @param groovyFile 对应文件
	 * @return
	 * @throws IOException
	 */
	public String register(File groovyFile) throws IOException {
		Class groovyClass = GroovyFactory.getInstance().compile(groovyFile);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(groovyClass)
				.applyCustomizers(bd -> {
					bd.setBeanClassName(GroovyScriptFactory.class.getName());
					bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, GroovyScriptCons.LANGUAGE);
					bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE,
							groovyProperties.getRefreshCheckDelaySecond());
					bd.setAutowireCandidate(true);
					// 设置 scriptSourceLocator 值
					bd.getConstructorArgumentValues().addIndexedArgumentValue(0, groovyFile.getAbsolutePath());
				});
		defaultListableBeanFactory.registerBeanDefinition(groovyClass.getSimpleName(),
				beanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.getDependentBeans(groovyClass.getSimpleName());
		return groovyClass.getSimpleName();
	}

	/**
	 * 注册脚本
	 * @param code
	 * @param className 对应文件名
	 * @return
	 */
	public String register(String code, String className) {
		Class groovyClass = GroovyFactory.getInstance().compile(code, className);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(groovyClass)
				.applyCustomizers(bd -> {
					bd.setBeanClassName(GroovyScriptFactory.class.getName());
					bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, GroovyScriptCons.LANGUAGE);
					bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE,
							groovyProperties.getRefreshCheckDelaySecond());
					bd.setAutowireCandidate(true);
					// 设置 scriptSourceLocator 值
					bd.getConstructorArgumentValues().addIndexedArgumentValue(0, className);
				});
		defaultListableBeanFactory.registerBeanDefinition(groovyClass.getSimpleName(),
				beanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.getDependentBeans(groovyClass.getSimpleName());
		return groovyClass.getSimpleName();
	}

	/**
	 * 移除容器中的bean
	 * @param beanName
	 */
	public void remove(String beanName) {
		defaultListableBeanFactory.removeBeanDefinition(beanName);
		defaultListableBeanFactory.clearMetadataCache();
	}

	@Override
	@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
	public void afterPropertiesSet() {
		Thread thread = new Thread("ScanGroovyScriptListener") {
			@Override
			public void run() {
				while (groovyProperties.getEnableScanScript()) {
					try {
						scanScript();
					}
					catch (Exception e) {
						log.error("扫描 groovy 脚本失败！", e);
					}
					ThreadUtil.sleep(groovyProperties.getScanScriptSecond(), TimeUnit.MILLISECONDS);
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

}
