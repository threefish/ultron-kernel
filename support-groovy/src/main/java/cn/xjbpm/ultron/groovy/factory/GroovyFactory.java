package cn.xjbpm.ultron.groovy.factory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/7/30
 */
public class GroovyFactory {

	private static final GroovyFactory GROOVY_FACTORY = new GroovyFactory();

	private static final GroovyClassLoader GROOVY_CLASS_LOADER = new GroovyClassLoader(
			ClassUtils.getDefaultClassLoader());

	public static GroovyFactory getInstance() {
		return GROOVY_FACTORY;
	}

	/**
	 * 从文件编译 groovy 类
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Class compile(File file) throws IOException {
		return GROOVY_CLASS_LOADER.parseClass(file);
	}

	/**
	 * 编译 Groovy 代码并返回编译代码的类
	 * @param script
	 * @param name 生成的类的名称
	 * @return
	 */
	public Class compile(String script, String name) {
		GroovyCodeSource groovyCodeSource = AccessController.doPrivileged(
				(PrivilegedAction<GroovyCodeSource>) () -> new GroovyCodeSource(script, name, "/groovy/script"));
		return GROOVY_CLASS_LOADER.parseClass(groovyCodeSource);
	}

}
